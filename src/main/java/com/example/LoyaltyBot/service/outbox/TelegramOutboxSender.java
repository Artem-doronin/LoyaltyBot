package com.example.LoyaltyBot.service.outbox;

import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.entity.outbox.OutboxMessage;
import com.example.LoyaltyBot.exception.TelegramOutboxException;
import com.example.LoyaltyBot.repository.ClientRepository;
import com.example.LoyaltyBot.repository.outbox.OutboxSender;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = "outbox.sender.type", havingValue = "telegram")
public class TelegramOutboxSender implements OutboxSender {
    private final ClientRepository clientRepository;
    private final TelegramClient telegramClient;
    @Override
    public void send(OutboxMessage outboxMessage) {
        Client client = clientRepository.findById(outboxMessage.getClientId()).orElseThrow(
                () -> new EntityNotFoundException("Client not found"));

        try {
            SendMessage message = SendMessage.builder()
                    .chatId(client.getChatId().toString())
                    .text(outboxMessage.getPayload())
                    .parseMode("HTML")
                    .build();

            telegramClient.execute(message);
            log.info("Message sent to chatId: {}", client.getChatId());
        } catch (TelegramApiException e) {
            throw new TelegramOutboxException("Failed to send message to chatId=" + client.getChatId(), e);
        }
    }
}
