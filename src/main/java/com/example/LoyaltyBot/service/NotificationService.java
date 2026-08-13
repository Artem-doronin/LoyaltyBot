package com.example.LoyaltyBot.service;

import java.math.BigDecimal;

public interface NotificationService {
    void send(Long clientId, String message);

    void notifyBonusEarned(Long chatId, String clientName,
                           BigDecimal bonusAmount, BigDecimal totalBalance);

    void notifyBonusSpent(Long chatId, String clientName,
                          BigDecimal spentAmount, BigDecimal remainingBalance);

    void notifyInsufficientBonuses(Long chatId, String clientName,
                                   BigDecimal requested, BigDecimal available);

    void notifyLoyaltyUpgrade(Long chatId, String clientName,
                              String oldTier, String newTier);

}
