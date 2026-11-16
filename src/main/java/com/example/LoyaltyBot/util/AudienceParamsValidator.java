package com.example.LoyaltyBot.util;

import com.example.LoyaltyBot.entity.campaigns.AudienceParams;
import com.example.LoyaltyBot.entity.campaigns.AudienceType;
import com.example.LoyaltyBot.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class AudienceParamsValidator {
    public void validate(AudienceType  type, AudienceParams params){
        switch (type) {
            case ALL_CLIENTS, BIRTHDAY -> {
                if (params != null && !params.isEmpty()) {
                    throw new ValidationException("Параметры не требуются для " + type);
                }
            }
            case GENDER -> {
                if (params == null || params.gender() == null) {
                    throw new ValidationException("Для GENDER обязателен параметр gender");
                }
            }
            case NO_PURCHASE_FOR_DAYS -> {
                if (params == null || params.days() == null || params.days() <= 0) {
                    throw new ValidationException("Для NO_PURCHASE_FOR_DAYS обязателен days > 0");
                }
            }
            case BONUS_BALANCE_GREATER_THAN -> {
                if (params == null || params.bonusBalance() == null
                        || params.bonusBalance().signum() < 0) {
                    throw new ValidationException("Для BONUS_BALANCE_GREATER_THAN обязателен bonusBalance >= 0");
                }
            }
        }

    }
}
