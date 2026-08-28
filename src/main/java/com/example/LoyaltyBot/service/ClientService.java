package com.example.LoyaltyBot.service;

import com.example.LoyaltyBot.dto.SuccessClientResponse;
import com.example.LoyaltyBot.dto.client.ClientResponseDto;
import com.example.LoyaltyBot.dto.client.ClientResponseSearchDto;
import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.mapper.ClientMapper;
import com.example.LoyaltyBot.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final ClientBonusBalancesService bonusService;
    private final JdbcTemplate jdbcTemplate;


    private static final int DEFAULT_LIMIT = 10;
    private static final int MAX_LIMIT = 20;

    public ClientService(ClientRepository clientRepository,
                         ClientMapper clientMapper,
                         ClientBonusBalancesService bonusService,
                         JdbcTemplate jdbcTemplate) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.bonusService = bonusService;
        this.jdbcTemplate = jdbcTemplate;
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


    public List<ClientResponseSearchDto> searchByPhone(String phone, int limit) {
        if (phone == null || phone.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String normalizedPhone = phone.trim();
        int normalizedLimit = normalizeLimit(limit);

        List<Client> clients = clientRepository.searchByPhoneWithLimit(normalizedPhone, normalizedLimit);

        return clients.stream()
                .map(ClientResponseSearchDto::fromDto)
                .toList();
    }

    public SuccessClientResponse findByPhoneNumber(String phone) {
        return getClientResponse("Клиент найден", phone);
    }

    private SuccessClientResponse getClientResponse(String message, String phone) {
        return jdbcTemplate.queryForObject(
                "SELECT c.id AS clientIdDb, c.first_name AS firstName, c.phone AS phone, "
                        + "COALESCE(cbb.amount, 0) AS bonusAmount, "
                        + "COALESCE(cbb.bonus_rate, 0) AS rate "
                        + "FROM clients c "
                        + "LEFT JOIN client_bonus_balances cbb ON c.id = cbb.client_id "
                        + "WHERE c.phone = ?",
                new RowMapper<SuccessClientResponse>() {
                    @Override
                    public SuccessClientResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Long clientDbId = rs.getLong("clientIdDb");
                        String firstName = rs.getString("firstName");
                        String phone = rs.getString("phone");
                        BigDecimal bonusAmount = rs.getBigDecimal("bonusAmount");
                        BigDecimal rate = rs.getBigDecimal("rate");
                        return new SuccessClientResponse(
                                clientDbId,
                                firstName,
                                phone,
                                bonusAmount,
                                rate,
                                message
                        );
                    }
                },
                phone
        );
    }

    private int normalizeLimit(int limit) {
        if (limit < 1) {
            return DEFAULT_LIMIT;
        }
        return Math.min(limit, MAX_LIMIT);
    }
}