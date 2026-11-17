package com.example.LoyaltyBot.handler.registration.ask;

import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.entity.RegistrationState;
import com.example.LoyaltyBot.entity.client.Gender;
import com.example.LoyaltyBot.handler.registration.RegistrationHandler;
import com.example.LoyaltyBot.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Component
@Slf4j
public class AskGenderRegistrationHandler implements RegistrationHandler {
    private final ClientService clientService;

    public AskGenderRegistrationHandler(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public SendMessage handle(Message message, Client client) {

        String gender = message.getText();
        log.debug(gender);
        System.out.println("_____________________");
        System.out.println(gender);

        if (gender.equals("gender_male")) client.setGender(Gender.MALE);
        if (gender.equals("gender_female")) client.setGender(Gender.FEMALE);

        client.setRegistrationState(RegistrationState.ASK_BIRTHDATE);

        clientService.updateClient(client);

        String messageResponse = String
                .format("Хорошо %s, теперь введите дату рождения в в формате dd.MM.yyyy", client.getFirstName());

        return SendMessage
                .builder()
                .chatId(message.getChatId())
                .text(messageResponse)
                .build();
    }
}
