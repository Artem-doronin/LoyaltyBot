package com.example.LoyaltyBot.handler.registration;

import com.example.LoyaltyBot.entity.Client;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
@Component
public class AskPhoneRegistrationHandler implements RegistrationHandler {
    @Override
    public SendMessage handle(String message, Long chatId, Client client) {
        return null;
    }
}
