package com.example.LoyaltyBot.config;

import com.example.LoyaltyBot.service.LoyaltyBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
public class TgBotConfig {
    @Value("${spring.telegram.bot.name}")
    private String name;
    @Value("${spring.telegram.bot.token}")
    private String token;

    @Bean
    public TelegramBotsLongPollingApplication telegramBotsLongPollingApplication(LoyaltyBot loyaltyBot)
            throws TelegramApiException {
        TelegramBotsLongPollingApplication telegramBotsLongPollingApplication = new TelegramBotsLongPollingApplication();
        telegramBotsLongPollingApplication.registerBot(token, loyaltyBot);
        return telegramBotsLongPollingApplication;
    }

    @Bean
    public TelegramClient telegramClient() {
        return new OkHttpTelegramClient(token);
    }
}
