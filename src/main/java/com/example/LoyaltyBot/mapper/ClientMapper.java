package com.example.LoyaltyBot.mapper;

import com.example.LoyaltyBot.dto.client.ClientCreateDto;
import com.example.LoyaltyBot.dto.client.ClientResponseDto;
import com.example.LoyaltyBot.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ClientMapper {
    ClientResponseDto toClientResponseDto(Client client);

    Client toClient(ClientCreateDto clientCreateDto);

}
