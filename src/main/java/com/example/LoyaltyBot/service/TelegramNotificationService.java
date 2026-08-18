package com.example.LoyaltyBot.service;

import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Service
@Slf4j
@AllArgsConstructor
public class TelegramNotificationService implements NotificationService {

    private final TelegramClient telegramClient;
    private final ClientRepository clientRepository;


    @Override
    public void send(Long clientId, String text) {

        Client client = clientRepository.findById(clientId).orElseThrow(
                () -> new EntityNotFoundException("Client not found"));

        try {
            SendMessage message = SendMessage.builder()
                    .chatId(client.getChatId().toString())
                    .text(text)
                    .parseMode("HTML")
                    .build();

            telegramClient.execute(message);
            log.info("Message sent to chatId: {}", client.getChatId());
        } catch (TelegramApiException e) {
            log.error("Failed to send message to chatId: {}", client.getChatId(), e);
        }
    }

    @Override
    public void send(SendMessage message) {
        try {
            telegramClient.execute(message);
            log.info("Message sent to chatId: {}", message.getChatId());
        } catch (TelegramApiException e) {
            log.error("Failed to send message to chatId: {}", message.getChatId(), e);
        }
    }
}