package com.example.LoyaltyBot.dto.client;

import com.example.LoyaltyBot.entity.Client;
import lombok.Builder;

@Builder
public record ClientResponseSearchDto(
        Long clientId,
        String firstName,
        String phoneNumber
) {

    public static ClientResponseSearchDto fromDto(Client client) {
        return ClientResponseSearchDto.builder()
                .clientId(client.getId())
                .firstName(client.getFirstName())
                .phoneNumber(client.getPhone())
                .build();
    }
}
