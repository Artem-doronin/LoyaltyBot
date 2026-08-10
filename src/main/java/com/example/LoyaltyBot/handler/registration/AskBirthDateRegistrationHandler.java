package com.example.LoyaltyBot.handler.registration;

import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.entity.RegistrationState;
import com.example.LoyaltyBot.service.ClientService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Component
public class AskBirthDateRegistrationHandler implements RegistrationHandler {
    private final ClientService clientService;


    public AskBirthDateRegistrationHandler(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public SendMessage handle(Message message, Client client) {

        String messageResponse;
        Optional<LocalDate> birthDate = parseBirthDate(message.getText());

        KeyboardButton phoneBaton = KeyboardButton
                .builder()
                .text("Поделиться номером телефона")
                .requestContact(true)
                .build();

        ReplyKeyboardMarkup keyboardMarkup = ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(List.of(phoneBaton)))
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .build();

        if (birthDate.isPresent()) {
            client.setBirthday(birthDate.get());
            client.setRegistrationState(RegistrationState.ASK_PHONE);

            clientService.updateClient(client);

            return SendMessage
                    .builder()
                    .chatId(message.getChatId())
                    .text(String
                            .format("Хорошо %s, теперь поделитесь номером телефона ",
                                    client.getFirstName()))
                    .replyMarkup(keyboardMarkup)
                    .build();

        } else {

            return SendMessage
                    .builder()
                    .chatId(message.getChatId())
                    .text("Дата рождения не валидна попробуйте еще")
                    .build();
        }
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
