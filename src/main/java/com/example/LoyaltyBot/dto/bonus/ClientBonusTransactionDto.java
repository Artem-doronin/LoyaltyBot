package com.example.LoyaltyBot.dto.bonus;

import com.example.LoyaltyBot.entity.ClientBonusTransactions;
import com.example.LoyaltyBot.entity.OperationType;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ClientBonusTransactionDto(
        Long clientId,
        String phoneNumber,
        BigDecimal bonusAmount,
        BigDecimal operationAmount ,
        OperationType operationType,
        String comment
) {
    public ClientBonusTransactions fromClientBonusTransactions(Long userId) {
        return  ClientBonusTransactions.builder()
                .operationAmount(operationAmount)
                .bonusAmount(bonusAmount)
                .clientId(clientId)
                .userId(userId)
                .operationType(operationType)
                .description(comment)
                .build();
    }
}
