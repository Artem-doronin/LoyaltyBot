package com.example.LoyaltyBot.handler.registration;

import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.entity.RegistrationState;
import com.example.LoyaltyBot.service.ClientService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Component
public class AskNameRegistrationHandler implements RegistrationHandler {
    private final ClientService clientService;

    public AskNameRegistrationHandler(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public SendMessage handle(Message message, Client client) {

        client.setRegistrationState(RegistrationState.ASK_BIRTHDATE);
        client.setFirstName(message.getText());

        clientService.updateClient(client);

        String messageResponse = String
                .format("Хорошо %s, теперь введите дату рождения в в формате dd.MM.yyyy", client.getFirstName() );

        return SendMessage
                .builder()
                .chatId(message.getChatId())
                .text(messageResponse)
                .build();
    }
}
