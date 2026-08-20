package com.example.LoyaltyBot.dto.bonus;

import com.example.LoyaltyBot.entity.OperationType;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record BonusOperationDto(
        Long clientId,
        String phoneNumber,
        BigDecimal bonusAmount,
        BigDecimal operationAmount ,
        OperationType operationType,
        String comment
) {
}
