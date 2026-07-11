package com.example.LoyaltyBot.handler;

import org.hibernate.sql.Update;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class UpdateMessageHandler {
    public SendMessage handle(String text ,Long chatId) {


        return SendMessage
                .builder()
                .chatId(chatId)
                .text(String.format("Вы написали %s ,но я пока не могу" +
                        " обрабатывать логику, мой создатель еще не написал ее мне", text))
                .build();
    }
}
