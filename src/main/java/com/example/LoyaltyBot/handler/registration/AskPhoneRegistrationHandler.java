package com.example.LoyaltyBot.handler.registration;

import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.entity.RegistrationState;
import com.example.LoyaltyBot.service.ClientService;
import com.example.LoyaltyBot.validator.PhoneValidator;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Optional;

@Component
public class AskPhoneRegistrationHandler implements RegistrationHandler {

    private final ClientService clientService;
    private final PhoneValidator phoneValidator;

    public AskPhoneRegistrationHandler(ClientService clientService, PhoneValidator phoneValidator) {
        this.clientService = clientService;
        this.phoneValidator = phoneValidator;
    }

    @Override
    public SendMessage handle(String message, Long chatId, Client client) {

        String sendMes;
        Optional<String> optional = phoneValidator.validate(message);

        if (optional.isPresent()) {
            client.setPhone(optional.get());
            client.setRegistrationState(RegistrationState.REGISTERED);
            clientService.createClient(client);
            sendMes = "Спасибо за регистрацию";
        } else {
            sendMes = "Номер не валиден попробуйте еще";
        }
        return SendMessage
                .builder()
                .chatId(chatId)
                .text(sendMes)
                .build();
    }


}
