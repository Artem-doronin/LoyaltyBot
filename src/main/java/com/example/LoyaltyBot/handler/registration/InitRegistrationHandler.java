package com.example.LoyaltyBot.handler.registration;

import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.entity.RegistrationState;
import com.example.LoyaltyBot.service.ClientService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
@Component
public class InitRegistrationHandler implements RegistrationHandler {
    private final ClientService clientService;

    public InitRegistrationHandler(ClientService clientService) {
        this.clientService = clientService;
    }
//todo я беру клиента я могу изменять клиента и сохранять его в базу из параметров либо как здесь
    // todo например client не null и его взяли из базы нужно поле 1 поменять что его
    // todo пересетить на нового и залить в бд  ?
    @Override
    public SendMessage handle(String message, Long chatId, Client client) {

        Client clientInit = new Client();
        clientInit.setChatId(chatId);
        clientInit.setRegistrationState(RegistrationState.ASK_NAME);
        clientInit.setIsActive(true);
        clientInit.setBonusBalance(0);
        clientInit.setTotalSpent(0);
        clientService.createClient(clientInit);

        String messageResponse = " Введите ваше имя";

        return SendMessage
                .builder()
                .text(messageResponse)
                .chatId(chatId)
                .build();
    }
}
