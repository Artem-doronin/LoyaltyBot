package com.example.LoyaltyBot.dto.client;

import java.time.LocalDate;


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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTelegramUsername() {
        return telegramUsername;
    }

    public void setTelegramUsername(String telegramUsername) {
        this.telegramUsername = telegramUsername;
    }
}

