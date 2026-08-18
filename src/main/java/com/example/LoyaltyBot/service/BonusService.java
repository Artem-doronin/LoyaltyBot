package com.example.LoyaltyBot.service;

import com.example.LoyaltyBot.entity.Bonus;
import com.example.LoyaltyBot.repository.BonusRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
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
    public Bonus updateBonusAmount(Long clientId, BigDecimal newAmount) {
        Bonus bonus = bonusRepository.findByClientIdWithLock(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Bonus not found"));
        bonus.setAmount(newAmount);
        return bonus;
    }

    @Transactional
    public Bonus updateBonusRate(Long clientId, BigDecimal newBonusRate) {
        Bonus bonus = bonusRepository.findByClientIdWithLock(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Bonus not found"));
        bonus.setBonusRate(newBonusRate);
        return bonus;
    }
}
