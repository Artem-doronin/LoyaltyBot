package com.example.LoyaltyBot.handler;

import com.example.LoyaltyBot.exception.MessageSenderException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class MessageSender {
    private final TelegramClient telegramClient;

    public MessageSender(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }

    public void sendMessage(SendMessage message) {
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            throw new MessageSenderException("Не удалось отправить сообщение");
        }
    }
}
