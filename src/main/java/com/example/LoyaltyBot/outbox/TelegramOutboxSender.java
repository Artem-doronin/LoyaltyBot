package com.example.LoyaltyBot.outbox;

import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.entity.campaigns.AttachmentType;
import com.example.LoyaltyBot.entity.outbox.AttachmentPayload;
import com.example.LoyaltyBot.entity.outbox.OutboxMessage;
import com.example.LoyaltyBot.exception.TelegramOutboxException;
import com.example.LoyaltyBot.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaDocument;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaVideo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = "outbox.sender.type", havingValue = "telegram")
public class TelegramOutboxSender implements OutboxSender {

    private static final int MEDIA_GROUP_MAX = 10;

    private final ClientRepository clientRepository;
    private final TelegramClient telegramClient;

    @Override
    public void send(OutboxMessage outboxMessage) {
        Client client = clientRepository.findById(outboxMessage.getClientId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Client not found: " + outboxMessage.getClientId()));

        try {
            List<AttachmentPayload> attachments = outboxMessage.getAttachments();
            if (attachments == null || attachments.isEmpty()) {
                sendText(client, outboxMessage.getPayload());
            } else {
                sendWithAttachments(client, outboxMessage.getPayload(), attachments);
            }
            log.info("Message sent to chatId={}, executionId={}",
                    client.getChatId(), outboxMessage.getCampaignExecutionId());
        } catch (TelegramApiException e) {
            throw new TelegramOutboxException(
                    "Failed to send message to chatId=" + client.getChatId(), e);
        }
    }

    private void sendText(Client client, String text) throws TelegramApiException {
        SendMessage message = SendMessage.builder()
                .chatId(client.getChatId().toString())
                .text(text)
                .build();
        telegramClient.execute(message);
    }

    private void sendWithAttachments(Client client, String text,
                                     List<AttachmentPayload> attachments) throws TelegramApiException {
        String chatId = client.getChatId().toString();

        List<AttachmentPayload> sorted = new ArrayList<>(attachments);
        sorted.sort(Comparator.comparing(
                a -> a.sortOrder() != null ? a.sortOrder() : Integer.MAX_VALUE));

        List<AttachmentPayload> groupable = new ArrayList<>();
        List<AttachmentPayload> audios = new ArrayList<>();

        for (AttachmentPayload a : sorted) {
            if (a.type() == AttachmentType.AUDIO) {
                audios.add(a);
            } else {
                groupable.add(a);
            }
        }

        sendGroupable(chatId, groupable);
        sendAudios(chatId, audios);
        sendText(client, text);
    }

    private void sendGroupable(String chatId,
                               List<AttachmentPayload> groupable) throws TelegramApiException {
        for (int i = 0; i < groupable.size(); i += MEDIA_GROUP_MAX) {
            List<AttachmentPayload> chunk = groupable.subList(
                    i, Math.min(i + MEDIA_GROUP_MAX, groupable.size()));

            if (chunk.size() == 1) {
                sendSingleMedia(chatId, chunk.get(0));
            } else {
                sendMediaGroup(chatId, chunk);
            }
        }
    }

    private void sendSingleMedia(String chatId,
                                 AttachmentPayload attachment) throws TelegramApiException {
        InputFile file = toInputFile(attachment);

        switch (attachment.type()) {
            case IMAGE -> telegramClient.execute(
                    SendPhoto.builder().chatId(chatId).photo(file).build());
            case VIDEO -> telegramClient.execute(
                    SendVideo.builder().chatId(chatId).video(file).build());
            case DOCUMENT -> telegramClient.execute(
                    SendDocument.builder().chatId(chatId).document(file).build());
            case AUDIO -> throw new IllegalStateException(
                    "AUDIO should be sent via sendAudios, not sendSingleMedia");
        }
    }

    private void sendMediaGroup(String chatId,
                                List<AttachmentPayload> chunk) throws TelegramApiException {
        List<InputMedia> medias = new ArrayList<>();
        for (AttachmentPayload a : chunk) {
            medias.add(toInputMedia(a));
        }
        SendMediaGroup group = SendMediaGroup.builder()
                .chatId(chatId)
                .medias(medias)
                .build();
        telegramClient.execute(group);
    }

    private void sendAudios(String chatId,
                            List<AttachmentPayload> audios) throws TelegramApiException {
        for (AttachmentPayload a : audios) {
            InputFile file = toInputFile(a);
            telegramClient.execute(
                    SendAudio.builder().chatId(chatId).audio(file).build());
        }
    }

    // ---------- Медиа-референсы ----------

    /**
     * Для одиночной отправки (SendPhoto/Video/Document/Audio) и SendAudio.
     * Приоритет: fileId → fileUrl (скачивание в память).
     * <p>
     * Когда появится MinIO — сюда добавится ветка: если fileId и fileUrl пусты,
     * брать fileKey и скачивать через MinioClient. Пока — IllegalArgumentException.
     */
    private InputFile toInputFile(AttachmentPayload a) {
        if (a.fileId() != null && !a.fileId().isBlank()) {
            return new InputFile(a.fileId());
        }
        if (a.fileUrl() != null && !a.fileUrl().isBlank()) {
            return downloadFromUrl(a);
        }
        throw new IllegalArgumentException(
                "Attachment has neither fileId nor fileUrl: sortOrder=" + a.sortOrder());
    }

    /**
     * Для медиа-группы (SendMediaGroup). InputMedia принимает String:
     * либо fileId, либо URL. Скачивание не нужно — Telegram сам заберёт по URL.
     */
    private InputMedia toInputMedia(AttachmentPayload a) {
        String mediaRef = resolveMediaRef(a);

        return switch (a.type()) {
            case IMAGE -> InputMediaPhoto.builder().media(mediaRef).build();
            case VIDEO -> InputMediaVideo.builder().media(mediaRef).build();
            case DOCUMENT -> InputMediaDocument.builder().media(mediaRef).build();
            case AUDIO -> throw new IllegalStateException(
                    "AUDIO is not supported in media groups");
        };
    }

    private String resolveMediaRef(AttachmentPayload a) {
        if (a.fileId() != null && !a.fileId().isBlank()) {
            return a.fileId();
        }
        if (a.fileUrl() != null && !a.fileUrl().isBlank()) {
            return a.fileUrl();
        }
        // Когда появится MinIO — генерировать presigned URL и возвращать его.
        throw new IllegalArgumentException(
                "Attachment has neither fileId nor fileUrl: sortOrder=" + a.sortOrder());
    }

    private InputFile downloadFromUrl(AttachmentPayload a) {
        try {
            URL url = new URL(a.fileUrl());
            byte[] bytes;
            try (InputStream in = url.openStream()) {
                bytes = in.readAllBytes();
            }
            String fileName = extractFileName(url, a);
            return new InputFile(new ByteArrayInputStream(bytes), fileName);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Failed to open fileUrl: " + a.fileUrl(), e);
        }
    }

    private String extractFileName(URL url, AttachmentPayload a) {
        String path = url.getPath();
        String name = path.substring(path.lastIndexOf('/') + 1);
        if (name.isBlank() || !name.contains(".")) {
            return "file_" + a.sortOrder() + "." + extensionFor(a.type());
        }
        return name;
    }

    private String extensionFor(AttachmentType type) {
        return switch (type) {
            case IMAGE -> "jpg";
            case VIDEO -> "mp4";
            case DOCUMENT -> "pdf";
            case AUDIO -> "mp3";
        };
    }
}