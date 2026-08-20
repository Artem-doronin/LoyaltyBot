package com.example.LoyaltyBot.service;

import com.example.LoyaltyBot.dto.client.ClientResponseDto;
import com.example.LoyaltyBot.dto.client.ClientResponseSearchDto;
import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.mapper.ClientMapper;
import com.example.LoyaltyBot.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final BonusService bonusService;

    public ClientService(ClientRepository clientRepository,
                         ClientMapper clientMapper,
                         BonusService bonusService) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.bonusService = bonusService;
    }

    @Transactional
    public void createClient(Client client) {
        clientRepository.save(client);
        bonusService.create(client.getId());
    }

    public void updateClient(Client client) {
        Objects.requireNonNull(client);
        clientRepository.save(client);
    }

    public ClientResponseDto findById(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Клиент с id " + id + " не найден"));

        return clientMapper.toClientResponseDto(client);
    }

    public Optional<Client> findByTelegramUserId(Long telegramUserId) {
        return clientRepository.findByTelegramUserId(telegramUserId);
    }

    public List<ClientResponseDto> findAll() {
        return clientRepository.findAll().stream()
                .map(clientMapper::toClientResponseDto)
                .toList();
    }

    public void deleteById(Long id) {
        clientRepository.deleteById(id);
    }


    public List<ClientResponseSearchDto> searchClients(String query) {
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String normalizedQuery = query.trim();
        List<Client> clients = clientRepository.searchByPhoneNumber(normalizedQuery);

        return clients.stream()
                .map(client -> {
                    BigDecimal bonusAmount = bonusService.getAmount(client.getId());
                    return ClientResponseSearchDto.fromDto(client, bonusAmount);
                })
                .collect(Collectors.toList());
    }

    public ClientResponseDto findByPhoneNumber(String phoneNumber) {
        return clientMapper.toClientResponseDto(clientRepository.findByPhone(phoneNumber)
                .orElseThrow(() -> new EntityNotFoundException("Клиент с номером " + phoneNumber + " не найден")));
    }
}
