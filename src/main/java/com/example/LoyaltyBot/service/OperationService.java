package com.example.LoyaltyBot.service;

import com.example.LoyaltyBot.entity.Operation;
import com.example.LoyaltyBot.entity.OperationType;
import com.example.LoyaltyBot.repository.OperationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor

public class OperationService {
    private final OperationRepository operationRepository;

    public void create(BigDecimal opAmount, OperationType opType,BigDecimal bonusAmount,
                       String description,Long clientId,Long userId) {
        Operation operation = new Operation();
        operation.setOperationAmount(opAmount);
        operation.setOperationType(opType);
        operation.setBonusAmount(bonusAmount);
        operation.setDescription(description);
        operation.setClientId(clientId);
        operation.setUserId(userId);
        operationRepository.save(operation);
    }
}
