package com.example.LoyaltyBot.entity;

public enum TransactionSource {
    BOT,        // Клиент через Telegram-бота
    ADMIN,      // Админ через панель управления
    API,        // Внешний API (касса, CRM)
    SYSTEM
}
