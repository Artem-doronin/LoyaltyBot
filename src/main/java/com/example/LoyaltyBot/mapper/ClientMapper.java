package com.example.LoyaltyBot.mapper;

import com.example.LoyaltyBot.dto.client.ClientCreateDto;
import com.example.LoyaltyBot.dto.client.ClientResponseDto;
import com.example.LoyaltyBot.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ClientMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "birthday", source = "birthday")
    @Mapping(target = "chatId", source = "chatId")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "bonusBalance", source = "bonusBalance")
    @Mapping(target = "telegramUsername", source = "telegramUsername")
    @Mapping(target = "isActive", source = "isActive")
    @Mapping(target = "totalSpent", source = "totalSpent")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    ClientResponseDto toClientResponseDto(Client client);

    Client toClient(ClientCreateDto clientCreateDto);

}
