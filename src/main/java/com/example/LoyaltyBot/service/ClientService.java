package com.example.LoyaltyBot.service;

import com.example.LoyaltyBot.dto.client.ClientResponseDto;
import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.mapper.ClientMapper;
import com.example.LoyaltyBot.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

}
