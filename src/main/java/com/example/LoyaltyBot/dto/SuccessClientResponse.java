package com.example.LoyaltyBot.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record SuccessClientResponse(
        Long id,
        String firstName,
        String phone,
        BigDecimal bonusAmount,
        BigDecimal rate,
        String message
) {
}
