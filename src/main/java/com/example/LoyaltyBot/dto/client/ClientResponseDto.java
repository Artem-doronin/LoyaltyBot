package com.example.LoyaltyBot.dto.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ClientResponseDto extends ClientBaseDto {
    private Long id;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private Boolean isActive;
    private Integer totalSpent;
    private Integer bonusBalance;

}
