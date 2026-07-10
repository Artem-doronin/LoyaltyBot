package com.example.LoyaltyBot.handler;

import org.telegram.telegrambots.meta.api.objects.Update;

public class CallbackHandler {
  private final MessageSender sender;

    public CallbackHandler(MessageSender sender) {
        this.sender = sender;
    }

    public void handle(Update update) {

    }
}
