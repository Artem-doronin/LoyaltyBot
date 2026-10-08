package com.example.LoyaltyBot.entity;

import java.util.List;

public enum OperationType {
    PURCHASE_ACCRUAL,
    PURCHASE_WRITE_OFF,
    REGISTRATION_BONUS,
    BONUS_EXPIRATION,
    MANUAL_ACCRUAL,
    MANUAL_WRITE_OFF;

    public static final List<OperationType> PURCHASE_TYPES =
            List.of(PURCHASE_ACCRUAL, PURCHASE_WRITE_OFF);

    public boolean isPurchase() {
        return PURCHASE_TYPES.contains(this);
    }
}
