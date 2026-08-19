package com.example.LoyaltyBot.dto.client;

import com.example.LoyaltyBot.entity.Client;
import lombok.Builder;

import java.math.BigDecimal;
@Builder
public record ClientResponseSearchDto(
        Long id,
        String fullName,
        String phoneNumber,
        BigDecimal bonusAmount) {

    public static ClientResponseSearchDto fromDto(Client client,BigDecimal bonusAmount) {
        return ClientResponseSearchDto.builder()
                .id(client.getId())
                .fullName(client.getFirstName())
                .phoneNumber(client.getPhone())
                .bonusAmount(bonusAmount != null ? bonusAmount : BigDecimal.ZERO)
                .build();
    }
}
