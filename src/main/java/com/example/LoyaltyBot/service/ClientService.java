package com.example.LoyaltyBot.service;

import com.example.LoyaltyBot.dto.client.ClientCreateDto;
import com.example.LoyaltyBot.dto.client.ClientResponseDto;
import com.example.LoyaltyBot.dto.client.ClientUpdateDto;
import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.exception.ClientNotFoundException;
import com.example.LoyaltyBot.mapper.ClientMapper;
import com.example.LoyaltyBot.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientService(ClientRepository clientRepository,
                         ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    public void createClient(ClientCreateDto clientCreateDto) {
        Client client = clientMapper.toClient(clientCreateDto);

        // дописать логику после телеги
        clientRepository.save(client);
    }

    public ClientResponseDto findById(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(
                () -> new ClientNotFoundException(id));
        log.info("client after mapping: id={}, isActive={}", client.getId(), client.getIsActive());
        ClientResponseDto dto = clientMapper.toClientResponseDto(client);

        // ✅ Логируем значение после маппинга
        log.info("DTO after mapping: id={}, isActive={}", dto.getId(), dto.getIsActive());
        return clientMapper.toClientResponseDto(client);

    }

    public List<ClientResponseDto> findAll() {
        List<ClientResponseDto> clientResponseDtos = clientRepository.findAll().stream()
                .map(clientMapper::toClientResponseDto)
                .toList();
        clientResponseDtos.stream().forEach(System.out::println);
    return clientResponseDtos;
    }

    public void deleteById(Long id) {
        clientRepository.deleteById(id);
    }

    public void updateClient(ClientUpdateDto updateDto, Long id) {
        Client client = clientRepository.findById(id).orElseThrow(
                () -> new ClientNotFoundException(id));

        client.setFirstName(updateDto.getFirstName());
        client.setLastName(updateDto.getLastName());
        client.setBirthday(updateDto.getBirthday());
        client.setPhone(updateDto.getPhone());
        clientRepository.save(client);
    }
}
