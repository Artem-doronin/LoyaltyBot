package com.example.LoyaltyBot.dto.client;

import java.time.LocalDate;

public class ClientUpdateDto {
    protected String firstName;
    protected String lastName;
    protected LocalDate birthday;
    protected String chatId;
    protected String phone;
    protected String telegramUsername;
    private String comment;
    private Boolean isActive;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getPhone() {
        return phone;
    }

    public String getChatId() {
        return chatId;
    }

    public String getTelegramUsername() {
        return telegramUsername;
    }

    public String getComment() {
        return comment;
    }

    public Boolean getActive() {
        return isActive;
    }
}

