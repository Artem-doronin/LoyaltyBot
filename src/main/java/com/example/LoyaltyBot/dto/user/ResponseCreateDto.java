package com.example.LoyaltyBot.dto.user;

import lombok.Builder;

@Builder
public record ResponseCreateDto(
        String login,
        String temporary_password
) {
}
