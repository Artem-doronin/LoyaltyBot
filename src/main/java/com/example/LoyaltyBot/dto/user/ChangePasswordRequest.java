package com.example.LoyaltyBot.dto.user;

import lombok.Builder;

@Builder
public record ChangePasswordRequest(
        String oldPassword,
        String newPassword,
        String confirmPassword
) {
}
