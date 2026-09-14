package com.example.LoyaltyBot.exception;

public class InsufficientBonusException extends RuntimeException {
    public InsufficientBonusException(String message) {
        super(message);
    }
}
