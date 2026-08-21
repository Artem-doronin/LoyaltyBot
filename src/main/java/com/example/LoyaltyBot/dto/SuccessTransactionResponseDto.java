package com.example.LoyaltyBot.dto;

import lombok.Builder;

import java.math.BigDecimal;
@Builder
public record SuccessTransactionResponseDto(
        Long clientId,
        BigDecimal newBalance,
        BigDecimal amount,
        BigDecimal operationAmount,
        String message
) {
}
