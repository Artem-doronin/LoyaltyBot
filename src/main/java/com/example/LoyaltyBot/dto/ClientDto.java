package com.example.LoyaltyBot.dto;

import com.example.LoyaltyBot.entity.Client;
import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record ClientDto(
        Long id,
        Long chatId,
        String telegramUsername,
        String firstName,
        String lastName,
        String phone,
        LocalDateTime birthday,
        Integer bonusBalance,
        Integer totalSpent) {


    public static ClientDto toDto(Client client){
        return  ClientDto.builder()
                .id(client.getId())
                .chatId(client.getChatId())
                .telegramUsername(client.getTelegramUsername())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .phone(client.getPhone())
                .birthday(client.getBirthday())
                .bonusBalance(client.getBonusBalance())
                .totalSpent(client.getTotalSpent())
                .build();
    }
    public Client toClient(){
        return Client.builder()
                .id(id)
                .chatId(chatId)
                .telegramUsername(telegramUsername)
                .firstName(firstName)
                .lastName(lastName)
                .phone(phone)
                .birthday(birthday)
                .bonusBalance(bonusBalance)
                .totalSpent(totalSpent)
                .build();
    }
    public String getLoyaltyLevel() {
        if (bonusBalance >= 1000) return "GOLD";
        if (bonusBalance >= 500) return "SILVER";
        return "BRONZE";
    }
}






