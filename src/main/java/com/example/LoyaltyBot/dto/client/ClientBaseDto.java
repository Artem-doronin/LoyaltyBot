package com.example.LoyaltyBot.dto.client;

import com.example.LoyaltyBot.entity.Client;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter


public abstract class ClientBaseDto {
    protected String firstName;
    protected String lastName;
    protected LocalDate birthday;
    protected String chatId;
    protected String phone;
    protected String telegramUsername;


    public ClientBaseDto(String firstName, String lastName, LocalDate birthday, String chatId, String phone, String telegramUsername) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.chatId = chatId;
        this.phone = phone;
        this.telegramUsername = telegramUsername;
    }

    public ClientBaseDto() {
    }
}

