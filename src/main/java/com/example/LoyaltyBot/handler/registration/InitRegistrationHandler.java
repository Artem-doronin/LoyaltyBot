package com.example.LoyaltyBot.handler.registration;

import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.entity.RegistrationState;
import com.example.LoyaltyBot.service.ClientService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Component
public class InitRegistrationHandler implements RegistrationHandler {
    private final ClientService clientService;

    public InitRegistrationHandler(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public SendMessage handle(Message message, Client client) {

        client.setChatId(message.getChatId());
        client.setTelegramUserId(message.getFrom().getId());
        client.setTelegramUsername(message.getFrom().getUserName());
        client.setRegistrationState(RegistrationState.ASK_NAME);
        client.setIsActive(true);
        client.setBonusBalance(0);
        client.setTotalSpent(0);
        clientService.createClient(client);

        String messageResponse = " Введите ваше имя";

        return SendMessage
                .builder()
                .text(messageResponse)
                .chatId(message.getChatId())
                .build();
    }
}
