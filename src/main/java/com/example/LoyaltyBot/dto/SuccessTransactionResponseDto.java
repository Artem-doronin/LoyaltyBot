package com.example.LoyaltyBot.dto;

import com.example.LoyaltyBot.dto.bonus.ClientBonusTransactionDto;
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
    public static SuccessTransactionResponseDto fromSuccessTransactionResponseDto(ClientBonusTransactionDto transactionDto,
                                                                           BigDecimal newBalance,
                                                                           String message) {
        return SuccessTransactionResponseDto.builder()
                .newBalance(newBalance)
                .message(message)
                .clientId(transactionDto.clientId())
                .operationAmount(transactionDto.operationAmount())
                .amount(transactionDto.bonusAmount())
                .build();
    }
}
