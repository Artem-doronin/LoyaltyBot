package com.example.LoyaltyBot.dto.user;

import lombok.Builder;

@Builder
public record PasswordChangeUserDto(
        String oldPassword,
        String newPassword,
        String confirmPassword
) {
}
