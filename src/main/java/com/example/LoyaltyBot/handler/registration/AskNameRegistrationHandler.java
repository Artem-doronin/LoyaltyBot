package com.example.LoyaltyBot.handler.registration;

import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.entity.RegistrationState;
import com.example.LoyaltyBot.service.ClientService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
@Component
public class AskNameRegistrationHandler implements RegistrationHandler {
    private final ClientService clientService;

    public AskNameRegistrationHandler(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public SendMessage handle(String message, Long chatId, Client client) {
        client.setRegistrationState(RegistrationState.ASK_BIRTHDATE);
        client.setFirstName(message);
        //todo что вызвать c clientService.updateClient() или
        //todo clientService.createClient() если у апдейт поля разные сетитьВ

        clientService.createClient(client);

        String messageResponse = String
                .format( "Хорошо %s, теперь введите дату рождения в в формате dd.MM.yyyy",message);

        return SendMessage
                .builder()
                .chatId(chatId)
                .text(messageResponse)
                .build();
    }
}
