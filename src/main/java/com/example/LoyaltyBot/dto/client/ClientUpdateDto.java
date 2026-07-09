package com.example.LoyaltyBot.dto.client;

import lombok.Getter;

import java.time.LocalDate;
@Getter
public class ClientUpdateDto extends ClientBaseDto {
    private String comment;
    private Boolean isActive;

    public ClientUpdateDto() {
        super();
    }

    public ClientUpdateDto(String firstName, String lastName, LocalDate birthday, String chatId, String phone,
                           String telegramUsername, String comment, Boolean isActive) {
        super(firstName, lastName, birthday, chatId, phone, telegramUsername);
        this.comment = comment;
        this.isActive = isActive;
    }

}

