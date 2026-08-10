package com.example.LoyaltyBot.dto.client;

import java.time.LocalDate;

public record ClientUpdateDto(

    String firstName,
    String lastName,
    LocalDate birthday,
     String chatId,
     String phone,
    String telegramUsername,
     String comment,
    Boolean isActive
){

}


