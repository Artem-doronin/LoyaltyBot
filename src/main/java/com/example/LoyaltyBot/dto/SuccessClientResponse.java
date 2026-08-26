package com.example.LoyaltyBot.dto;

import com.example.LoyaltyBot.dto.bonus.BonusResponseDto;
import com.example.LoyaltyBot.entity.Client;
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
    public static SuccessClientResponse fromSuccess(Client client,
                                                    BonusResponseDto bonusResponseDto,
                                                    String message) {
        return SuccessClientResponse.builder()
                .id(client.getId())
                .firstName(client.getFirstName())
                .phone(client.getPhone())
                .bonusAmount(bonusResponseDto.amount())
                .rate(bonusResponseDto.rate())
                .message(message)
                .build();
    }
}
