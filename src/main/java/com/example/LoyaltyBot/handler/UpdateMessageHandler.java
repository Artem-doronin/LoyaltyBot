package com.example.LoyaltyBot.handler;

import com.example.LoyaltyBot.entity.Client;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Optional;

@Service
public class UpdateMessageHandler {
    public SendMessage handle(String text , Long chatId, Optional<Client> clientOptional) {
        Client client = clientOptional.orElse(new Client());

        return SendMessage
                .builder()
                .chatId(chatId)
                .text(String.format("%s вы написали %s ,но я пока не могу" +
                        " обрабатывать логику, мой создатель еще не написал ее мне",client.getFirstName(), text))
                .build();
    }
}
