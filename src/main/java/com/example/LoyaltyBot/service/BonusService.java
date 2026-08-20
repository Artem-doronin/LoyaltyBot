package com.example.LoyaltyBot.service;

import com.example.LoyaltyBot.dto.bonus.BonusResponseDto;
import com.example.LoyaltyBot.entity.Bonus;
import com.example.LoyaltyBot.exception.InsufficientBonusException;
import com.example.LoyaltyBot.repository.BonusRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Slf4j
public class BonusService {
    private final BonusRepository bonusRepository;

    public BonusService(BonusRepository bonusRepository) {
        this.bonusRepository = bonusRepository;
    }

    public Bonus create(Long clientId) {
        Bonus newBonus = Bonus.builder()
                .clientId(clientId)
                .build();
        return bonusRepository.save(newBonus);
    }

    @Transactional
    public Bonus enrollmentBonuses(Long clientId, BigDecimal amount) {
        Bonus bonus = bonusRepository.findByClientIdWithLock(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Бонус не найден"));

        BigDecimal newAmount = bonus.getAmount().add(amount);
        bonus.setAmount(newAmount);

        log.info("Начислено {} бонусов клиенту {}", amount, clientId);
        return bonus;
    }


    @Transactional
    public Bonus writeOffBonuses(Long clientId, BigDecimal amount) {
        Bonus bonus = bonusRepository.findByClientIdWithLock(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Бонус не найден"));

        if (bonus.getAmount().compareTo(amount) < 0) {
            throw new InsufficientBonusException(
                    "Недостаточно бонусов. Доступно: " + bonus.getAmount()
            );
        }

        BigDecimal newAmount = bonus.getAmount().subtract(amount);
        bonus.setAmount(newAmount);

        log.info("Списано {} бонусов у клиента {}", amount, clientId);
        return bonus;
    }

    public BigDecimal getAmount(Long clientId) {
        return bonusRepository.findAmountByClientId(clientId).orElse(BigDecimal.ZERO);
    }

    public BonusResponseDto getBonusDto(Long clientId) {
        Bonus bonus = bonusRepository.findByClientId(clientId).orElseThrow(
                () -> new EntityNotFoundException("Bonus not found"));
        return BonusResponseDto.fromDto(bonus);
    }
}
