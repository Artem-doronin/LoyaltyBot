package com.example.LoyaltyBot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Service
public class LoyaltyBot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;

    public LoyaltyBot(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            System.out.println(message);
            long chatId = update.getMessage().getChatId();

            String mesageToSend = "/start".equals(message) ? "Привет я бот ,я повторяю за тобой" : message;

            SendMessage sendMessage = SendMessage
                    .builder()
                    .chatId(chatId)
                    .text(mesageToSend)
                    .replyMarkup(InlineKeyboardMarkup
                            .builder()
                            .keyboardRow(new InlineKeyboardRow(InlineKeyboardButton
                                    .builder()
                                    .text("Нажми меня 1")
                                    .callbackData("Callback_data_1")
                                    .build()))
                            .keyboardRow(new InlineKeyboardRow(InlineKeyboardButton
                                    .builder()
                                    .text("Нажми меня 2")
                                    .callbackData("Callback_data_2")
                                    .build()))
                            .build())
                    .build();
            try {
                telegramClient.execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        }else if(update.hasCallbackQuery()){
            long chat_id = update.getCallbackQuery().getMessage().getChatId();
            String callback_query = update.getCallbackQuery().getData();
            String messageToSend = String.format("Вы нажали %s ", callback_query );

            SendMessage sendMessage = SendMessage
                    .builder()
                    .chatId(chat_id)
                    .text(messageToSend)
                    .build();

            try {
                telegramClient.execute(sendMessage);
            }catch (TelegramApiException e){
                e.printStackTrace();
            }
        }
    }
}
