package com.example.LoyaltyBot.handler.registration;

import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.entity.RegistrationState;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ClientRegistrationHandler {

    private final Map<RegistrationState, RegistrationHandler> registrationHandlerMap = new HashMap<>();

    public ClientRegistrationHandler(AskBirthDateRegistrationHandler askBirthDateRegistrationHandler,
                                     AskNameRegistrationHandler askNameRegistrationHandler,
                                     AskPhoneRegistrationHandler askPhoneRegistrationHandler,
                                     InitRegistrationHandler initRegistrationHandler) {
        registrationHandlerMap.put(RegistrationState.INIT, initRegistrationHandler);
        registrationHandlerMap.put(RegistrationState.ASK_NAME, askNameRegistrationHandler);
        registrationHandlerMap.put(RegistrationState.ASK_BIRTHDATE, askBirthDateRegistrationHandler);
        registrationHandlerMap.put(RegistrationState.ASK_PHONE, askPhoneRegistrationHandler);
    }

    public SendMessage register(String message, Long chatId, Optional<Client> optionalClient) {
        RegistrationState registrationState = optionalClient
                .map(Client::getRegistrationState)
                .orElse(RegistrationState.INIT);

        Client client = optionalClient.orElse(new Client());

        RegistrationHandler registrationHandler = registrationHandlerMap.get(registrationState);
        return registrationHandler.handle(message,chatId,client);
    }

}
