package com.example.LoyaltyBot.exception;

public class MessageSenderException extends RuntimeException {

    public MessageSenderException() {
        super();
    }
    public MessageSenderException(String message) {
        super(message);
    }
}
