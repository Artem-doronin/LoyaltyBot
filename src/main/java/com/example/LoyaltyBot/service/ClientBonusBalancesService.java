package com.example.LoyaltyBot.service;

import com.example.LoyaltyBot.dto.bonus.BonusResponseDto;
import com.example.LoyaltyBot.entity.ClientBonusBalances;
import com.example.LoyaltyBot.exception.InsufficientBonusException;
import com.example.LoyaltyBot.repository.ClientBonusBalancesRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Slf4j
public class ClientBonusBalancesService {
    private final ClientBonusBalancesRepository bonusRepository;

    public ClientBonusBalancesService(ClientBonusBalancesRepository bonusRepository) {
        this.bonusRepository = bonusRepository;
    }

    public ClientBonusBalances create(Long clientId) {
        ClientBonusBalances newBonus = ClientBonusBalances.builder()
                .clientId(clientId)
                .build();
        return bonusRepository.save(newBonus);
    }

    @Transactional
    public BigDecimal enrollmentBonuses(Long clientId, BigDecimal amount) {
        ClientBonusBalances bonus = bonusRepository.findByClientIdWithLock(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Бонус не найден"));

        BigDecimal newAmount = bonus.getAmount().add(amount);
        bonus.setAmount(newAmount);

        log.info("Начислено {} бонусов клиенту {}", amount, clientId);
        return newAmount;
    }


    @Transactional
    public BigDecimal writeOffBonuses(Long clientId, BigDecimal amount) {
        ClientBonusBalances bonus = bonusRepository.findByClientIdWithLock(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Бонус не найден"));

        if (bonus.getAmount().compareTo(amount) < 0) {
            throw new InsufficientBonusException(
                    "Недостаточно бонусов. Доступно: " + bonus.getAmount()
            );
        }

        BigDecimal newAmount = bonus.getAmount().subtract(amount);
        bonus.setAmount(newAmount);

        log.info("Списано {} бонусов у клиента {}", amount, clientId);
        return newAmount;
    }

    public BigDecimal getAmount(Long clientId) {
        return bonusRepository.findAmountByClientId(clientId).orElse(BigDecimal.ZERO);
    }

    public BonusResponseDto getBonusDto(Long clientId) {
        ClientBonusBalances bonus = bonusRepository.findByClientId(clientId).orElseThrow(
                () -> new EntityNotFoundException("Bonus not found"));
        return BonusResponseDto.fromDto(bonus);
    }
}
