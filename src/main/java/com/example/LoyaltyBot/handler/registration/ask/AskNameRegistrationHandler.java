package com.example.LoyaltyBot.handler.registration.ask;

import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.entity.RegistrationState;
import com.example.LoyaltyBot.handler.registration.RegistrationHandler;
import com.example.LoyaltyBot.service.ClientService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

@Component
public class AskNameRegistrationHandler implements RegistrationHandler {
    private final ClientService clientService;

    public AskNameRegistrationHandler(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public SendMessage handle(Message message, Client client) {

        client.setRegistrationState(RegistrationState.GENDER);
        client.setFirstName(message.getText());

        clientService.updateClient(client);

        String messageResponse = "Пожалуйста, выберите ваш пол:";


        InlineKeyboardButton maleBtn = InlineKeyboardButton.builder()
                .text("👨 Мужской")
                .callbackData("gender_male")
                .build();


        InlineKeyboardButton femaleBtn = InlineKeyboardButton.builder()
                .text("👩 Женский")
                .callbackData("gender_female")
                .build();

        InlineKeyboardRow row = new InlineKeyboardRow();
        row.add(maleBtn);
        row.add(femaleBtn);

        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder()
                .keyboard(List.of(row))
                .build();

        message.setReplyMarkup(markup);

        return SendMessage
                .builder()
                .chatId(message.getChatId())
                .replyMarkup(markup)
                .text(messageResponse)
                .build();
    }
}
