package com.example.LoyaltyBot.validator;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PhoneValidator {
    private static final String PHONE_PATTERN = "^[78]\\d{10}$";

    public Optional<String> validate(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return Optional.empty();
        }

        String normalizedPhone = phone.replaceAll("[^\\d+]", "");

        if (normalizedPhone.startsWith("+7")) {
            normalizedPhone = "7" + normalizedPhone.substring(2);
        } else if (normalizedPhone.startsWith("8")) {
            normalizedPhone = "8" + normalizedPhone.substring(1);
        } else {
            return Optional.empty();
        }
        if (!normalizedPhone.matches(PHONE_PATTERN)) {
            return Optional.empty();
        }
        return Optional.of(normalizedPhone);
    }
}
