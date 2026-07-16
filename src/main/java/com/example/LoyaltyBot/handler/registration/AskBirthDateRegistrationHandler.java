package com.example.LoyaltyBot.handler.registration;

import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.entity.RegistrationState;
import com.example.LoyaltyBot.service.ClientService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Component
public class AskBirthDateRegistrationHandler implements RegistrationHandler {
    private final ClientService clientService;


    public AskBirthDateRegistrationHandler(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public SendMessage handle(String message, Long chatId, Client client) {
        String messageResponse;
        Optional<LocalDate> birthDate = parseBirthDate(message);

        if (birthDate.isPresent()) {
            client.setBirthday(birthDate.get());
            client.setRegistrationState(RegistrationState.ASK_PHONE);

            clientService.updateClient(client);
            messageResponse = String
                    .format("Хорошо %s, теперь введите номер телефона 11 чисел в формате 89********* ", client.getFirstName());
        } else {
            messageResponse = "Дата рождения не валидна попробуйте еще";
        }
        return SendMessage
                .builder()
                .chatId(chatId)
                .text(messageResponse)
                .build();
    }

    private Optional<LocalDate> parseBirthDate(String date) {
        if (date == null || date.trim().isEmpty()) return Optional.empty();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            return Optional.of(LocalDate.parse(date, formatter));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }
}
