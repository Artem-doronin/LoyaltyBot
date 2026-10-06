package com.example.LoyaltyBot.entity.campaigns;

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
