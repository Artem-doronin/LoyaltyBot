package com.example.LoyaltyBot.dto.user;

import lombok.Builder;

@Builder
public record TemporaryPasswordResponse(
        String login,
        String temporaryPassword
) {

    public static TemporaryPasswordResponse toDto(String username, String password) {
        return TemporaryPasswordResponse.builder()
                .login(username)
                .temporaryPassword(password)
                .build();
    }
}
