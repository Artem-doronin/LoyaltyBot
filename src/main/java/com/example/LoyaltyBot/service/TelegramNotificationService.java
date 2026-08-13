package com.example.LoyaltyBot.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.math.BigDecimal;

@Service
@Slf4j
@AllArgsConstructor
public class TelegramNotificationService implements NotificationService {

    private final TelegramClient telegramClient;


    @Override
    public void send(Long chatId, String text) {
        try {
            SendMessage message = SendMessage.builder()
                    .chatId(chatId.toString())
                    .text(text)
                    .parseMode("HTML")
                    .build();

            telegramClient.execute(message);
            log.info("Message sent to chatId: {}", chatId);
        } catch (TelegramApiException e) {
            log.error("Failed to send message to chatId: {}", chatId, e);
        }
    }


    /**
     * Отправка уведомления о начислении бонусов
     */
    @Override
    public void notifyBonusEarned(Long chatId, String clientName,
                                  BigDecimal bonusAmount, BigDecimal totalBalance) {
        String text = String.format("""
                        <b>Бонусы начислены!</b>
                        
                        Уважаемый %s,
                        
                        Вам начислено <b>%.2f</b> бонусных баллов.
                        Текущий баланс: <b>%.2f</b> баллов.
                        
                        Спасибо за покупку!
                        """,
                clientName,
                bonusAmount,
                totalBalance);

        send(chatId, text);
    }

    /**
     * Отправка уведомления о списании бонусов
     */
    @Override
    public void notifyBonusSpent(Long chatId, String clientName,
                                 BigDecimal spentAmount, BigDecimal remainingBalance) {
        String text = String.format("""
                        <b>Бонусы списаны</b>
                        
                        Уважаемый %s,
                        
                        Списано <b>%.2f</b> бонусных баллов.
                        Остаток: <b>%.2f</b> баллов.
                        
                        Вы использовали свои бонусы!
                        """,
                clientName,
                spentAmount,
                remainingBalance);

        send(chatId, text);
    }

    /**
     * Отправка уведомления о недостатке бонусов
     */
    @Override
    public void notifyInsufficientBonuses(Long chatId, String clientName,
                                          BigDecimal requested, BigDecimal available) {
        String text = String.format("""
                        ⚠ <b>Недостаточно бонусов</b>
                        
                        Уважаемый %s,
                        
                        Запрошено: <b>%.2f</b> баллов
                        Доступно: <b>%.2f</b> баллов
                        
                        К сожалению, у вас недостаточно бонусов для этой операции.
                        """,
                clientName,
                requested,
                available);

        send(chatId, text);
    }

    /**
     * Отправка уведомления об изменении уровня лояльности
     */
    @Override
    public void notifyLoyaltyUpgrade(Long chatId, String clientName,
                                     String oldTier, String newTier) {
        String text = String.format("""
                         <b>Новый уровень лояльности!</b>
                        
                        Поздравляем, %s!
                        
                        Ваш уровень повысился с <b>%s</b> до <b>%s</b>!
                        
                        Теперь вам доступны лучшие условия и повышенный процент начисления бонусов.
                        """,
                clientName,
                oldTier,
                newTier);

        send(chatId, text);
    }
}
