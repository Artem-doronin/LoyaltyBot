package com.example.LoyaltyBot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface NotificationService {
    void send(Long clientId, String message);
    void send(SendMessage sendMessage);

}
