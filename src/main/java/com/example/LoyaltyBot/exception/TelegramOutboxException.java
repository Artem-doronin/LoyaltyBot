package com.example.LoyaltyBot.exception;

public class TelegramOutboxException  extends RuntimeException{
    public TelegramOutboxException(String message) {
        super(message);
    }

    public TelegramOutboxException(String message, Throwable cause) {
        super(message, cause);
    }
}
