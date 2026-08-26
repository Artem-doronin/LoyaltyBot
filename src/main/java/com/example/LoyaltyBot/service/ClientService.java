package com.example.LoyaltyBot.service;

import com.example.LoyaltyBot.dto.SuccessClientResponse;
import com.example.LoyaltyBot.dto.bonus.BonusResponseDto;
import com.example.LoyaltyBot.dto.client.ClientResponseDto;
import com.example.LoyaltyBot.dto.client.ClientResponseSearchDto;
import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.mapper.ClientMapper;
import com.example.LoyaltyBot.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final ClientBonusBalancesService bonusService;


    private static final int DEFAULT_LIMIT = 10;
    private static final int MAX_LIMIT = 20;

    public ClientService(ClientRepository clientRepository,
                         ClientMapper clientMapper,
                         ClientBonusBalancesService bonusService) {
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

    public SuccessClientResponse findByPhoneNumber(String phoneNumber) {

        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Номер телефона не может быть пустым");
        }

        String normalizedPhone = phoneNumber.trim();

        Client client = clientRepository.findByPhone(normalizedPhone)
                .orElseThrow(() -> new EntityNotFoundException("Клиент с номером " + phoneNumber + " не найден"));

        BonusResponseDto bonus = bonusService.getBonusDto(client.getId());

        return SuccessClientResponse.fromSuccess(client, bonus, "Клиент найден!");
    }


    public List<ClientResponseSearchDto> searchByPhone(String phone, int limit) {
        if (phone == null || phone.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String normalizedPhone = phone.trim();
        int normalizedLimit = normalizeLimit(limit);

        List<Client> clients = clientRepository.searchByPhoneWithLimit(
                normalizedPhone,
                normalizedLimit
        );

        return clients.stream()
                .map(client -> {
                    BigDecimal bonusAmount = bonusService.getAmount(client.getId());
                    return ClientResponseSearchDto.fromDto(client, bonusAmount);
                })
                .collect(Collectors.toList());
    }

    private int normalizeLimit(int limit) {
        if (limit < 1) {
            return DEFAULT_LIMIT;
        }
        return Math.min(limit, MAX_LIMIT);
    }
}