package com.example.LoyaltyBot.entity.campaigns;

import com.example.LoyaltyBot.entity.client.Gender;

import java.math.BigDecimal;

public record AudienceParams(
        Gender gender,
        Integer days,
        BigDecimal bonusBalance
) {
    public boolean isEmpty() {
        return gender == null && days == null && bonusBalance == null;
    }
}
