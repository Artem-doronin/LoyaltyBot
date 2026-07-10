package com.example.LoyaltyBot.handler;

import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.entity.RegistrationState;
import com.example.LoyaltyBot.exception.ClientNotFoundException;
import com.example.LoyaltyBot.service.ClientService;
import com.example.LoyaltyBot.service.LoyaltyBot;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Service
public class MessageHandler {
    private final ClientService clientService;

    public MessageHandler(ClientService clientService) {
        this.clientService = clientService;
    }

    public void handle(Update update) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        Optional<Client> clientOptional = clientService.findByChatId(chatId);
        if (isClientRegistered(clientOptional)) {
            return updateMessageHandler.handle(text, chatId);
        }else {
            return returnclientRegistrationHandler.register(text, chatId, clientOptional);
        }

    }

    private Boolean isClientRegistered(Optional<Client> clientOptional) {
        return clientOptional.map(client -> client.getRegistrationState()
                .equals(RegistrationState.REGISTERED)).orElse(false);
    }
}
