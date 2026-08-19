package com.example.LoyaltyBot.dto.bonus;

import com.example.LoyaltyBot.entity.Bonus;
import lombok.Builder;

import java.math.BigDecimal;
@Builder
public record BonusResponseDto(
        Long id,
        Long clientId,
        BigDecimal amount,
        BigDecimal rate
) {
    public static BonusResponseDto fromDto(Bonus bonus) {
        return BonusResponseDto.builder()
                .id(bonus.getId())
                .clientId(bonus.getClientId())
                .amount(bonus.getAmount())
                .rate(bonus.getBonusRate())
                .build();
    }
}
