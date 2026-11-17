package com.example.LoyaltyBot.handler.registration;

import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.entity.RegistrationState;
import com.example.LoyaltyBot.handler.registration.ask.AskBirthDateRegistrationHandler;
import com.example.LoyaltyBot.handler.registration.ask.AskGenderRegistrationHandler;
import com.example.LoyaltyBot.handler.registration.ask.AskNameRegistrationHandler;
import com.example.LoyaltyBot.handler.registration.ask.AskPhoneRegistrationHandler;
import com.example.LoyaltyBot.handler.registration.ask.InitRegistrationHandler;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ClientRegistrationHandler {

    private final Map<RegistrationState, RegistrationHandler> registrationHandlerMap = new HashMap<>();

    public ClientRegistrationHandler(AskBirthDateRegistrationHandler askBirthDateRegistrationHandler,
                                     AskNameRegistrationHandler askNameRegistrationHandler,
                                     AskPhoneRegistrationHandler askPhoneRegistrationHandler,
                                     InitRegistrationHandler initRegistrationHandler,
                                     AskGenderRegistrationHandler askGenderRegistrationHandler) {
        registrationHandlerMap.put(RegistrationState.INIT, initRegistrationHandler);
        registrationHandlerMap.put(RegistrationState.ASK_NAME, askNameRegistrationHandler);
        registrationHandlerMap.put(RegistrationState.ASK_BIRTHDATE, askBirthDateRegistrationHandler);
        registrationHandlerMap.put(RegistrationState.ASK_PHONE, askPhoneRegistrationHandler);
        registrationHandlerMap.put(RegistrationState.GENDER,askGenderRegistrationHandler);
    }

    public SendMessage register(Message message, Optional<Client> optionalClient) {
        RegistrationState registrationState = optionalClient
                .map(Client::getRegistrationState)
                .orElse(RegistrationState.INIT);

        Client client = optionalClient.orElse(new Client());

        RegistrationHandler registrationHandler = registrationHandlerMap.get(registrationState);
        return registrationHandler.handle(message, client);
    }

}
