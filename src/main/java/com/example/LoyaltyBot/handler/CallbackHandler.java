package com.example.LoyaltyBot.handler;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class CallbackHandler {
    private final MessageSender sender;

    public CallbackHandler(MessageSender sender) {
        this.sender = sender;
    }

    public SendMessage handle(Update update) {
return null;
    }
}
