package com.example.LoyaltyBot.exception;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException() {
        super();
    }
    public ClientNotFoundException(Long id) {
        super("Клиент с ID " + id + " не найден");
    }

    public ClientNotFoundException(String message) {
        super(message);
    }

    public ClientNotFoundException(Long id, Throwable cause) {
        super("Клиент с ID " + id + " не найден", cause);
    }
}
