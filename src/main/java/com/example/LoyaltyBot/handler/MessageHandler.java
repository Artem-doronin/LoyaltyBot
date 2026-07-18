package com.example.LoyaltyBot.handler;

import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.entity.RegistrationState;
import com.example.LoyaltyBot.handler.registration.ClientRegistrationHandler;
import com.example.LoyaltyBot.service.ClientService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Optional;

@Service
public class MessageHandler {
    private final ClientService clientService;
    private final ClientRegistrationHandler clientRegistrationHandler;
    private final UpdateMessageHandler updateMessageHandler;

    public MessageHandler(ClientService clientService,
                          ClientRegistrationHandler clientRegistrationHandler,
                          UpdateMessageHandler updateMessageHandler) {
        this.clientService = clientService;
        this.clientRegistrationHandler = clientRegistrationHandler;
        this.updateMessageHandler = updateMessageHandler;
    }

    public SendMessage handle(Update update) {
        Message message = update.getMessage();
        Long telegramUserId = message.getFrom().getId();
        Optional<Client> clientOptional = clientService.findByTelegramUserId(telegramUserId);




        if (isClientRegistered(clientOptional)) {
            return updateMessageHandler.handle(message,clientOptional);
        }else {
            return  clientRegistrationHandler.register(message, clientOptional);
        }

    }

    private Boolean isClientRegistered(Optional<Client> clientOptional) {
        return clientOptional.map(client -> client.getRegistrationState()
                .equals(RegistrationState.REGISTERED)).orElse(false);
    }
}
