package com.example.LoyaltyBot.dto.bonus;

import com.example.LoyaltyBot.entity.OperationType;
import lombok.Builder;

import java.math.BigDecimal;
@Builder
public record BonusOperationDto(
        String phoneNumber,
        BigDecimal amount,
        OperationType operation,
        String comment)
{
}
