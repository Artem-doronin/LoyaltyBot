package com.example.LoyaltyBot.dto.client;

import com.example.LoyaltyBot.entity.RegistrationState;

import java.time.LocalDate;

public record ClientResponseDto(
        Long id,
        String firstName,
        String lastName,
        LocalDate birthday,
        Long chatId,
        Long telegramUserId,
        String phone,
        String telegramUsername,
        LocalDate createdAt,
        LocalDate updatedAt,
        Boolean isActive,
        RegistrationState registrationState
) {
}
