package com.example.LoyaltyBot.handler.registration;

import com.example.LoyaltyBot.entity.Client;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Component
public interface RegistrationHandler {
    SendMessage handle(Message message, Client client);
}
