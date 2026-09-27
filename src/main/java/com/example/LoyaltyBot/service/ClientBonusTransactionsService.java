package com.example.LoyaltyBot.service;

import com.example.LoyaltyBot.dto.SuccessTransactionResponseDto;
import com.example.LoyaltyBot.dto.bonus.ClientBonusTransactionDto;
import com.example.LoyaltyBot.entity.OperationType;
import com.example.LoyaltyBot.entity.User;
import com.example.LoyaltyBot.exception.InsufficientBonusException;
import com.example.LoyaltyBot.repository.ClientBonusTransactionsRepository;
import com.example.LoyaltyBot.service.outbox.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j

public class ClientBonusTransactionsService {
    private final ClientBonusTransactionsRepository clientBonusTransactionsRepository;
    private final ClientBonusBalancesService clientBonusBalancesService;
    private final OutboxService outboxService;
    private final UserService userService;

    private static final String DEFAULT_COMMENT = "Без комментария";

    @Transactional
    public SuccessTransactionResponseDto enroll(ClientBonusTransactionDto dto) {
        validationTransaction(dto);
        BigDecimal newAmount = clientBonusBalancesService.enrollmentBonuses(dto.clientId(), dto.bonusAmount());
        log.info("Начислено {} бонусов клиенту {}", dto.bonusAmount(), dto.clientId());
        User user = userService.getCurrentUser();
        saveTransaction(dto, user.getId());
        String message = String.format("Начислено %s бонусов!", dto.bonusAmount());
        outboxService.enqueue(dto.clientId(), createNotificationMessage(dto, newAmount));
        return SuccessTransactionResponseDto.fromSuccessTransactionResponseDto(dto, newAmount, message);
    }

    @Transactional
    public SuccessTransactionResponseDto writeOff(ClientBonusTransactionDto dto) {
        validationTransaction(dto);
        BigDecimal newAmount = clientBonusBalancesService.writeOffBonuses(dto.clientId(), dto.bonusAmount());
        log.info("Списано {} бонусов клиенту {}", dto.bonusAmount(), dto.clientId());
        User user = userService.getCurrentUser();
        saveTransaction(dto, user.getId());
        String message = String.format(String.format("Списано %s бонусов!", dto.bonusAmount()));
        outboxService.enqueue(dto.clientId(), createNotificationMessage(dto, newAmount));
        return SuccessTransactionResponseDto.fromSuccessTransactionResponseDto(dto, newAmount, message);
    }

    private void validationTransaction(ClientBonusTransactionDto dto) {
        if (dto.clientId() == null) {
            throw new IllegalArgumentException("ID клиента обязателен");
        }
        if (dto.bonusAmount() == null || dto.bonusAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InsufficientBonusException("Сумма должна быть больше нуля");
        }
    }

    private void saveTransaction(ClientBonusTransactionDto dto, Long userId) {
        clientBonusTransactionsRepository.save(dto.fromClientBonusTransactions(userId));
    }

    private String createNotificationMessage(ClientBonusTransactionDto dto, BigDecimal newBalance) {
        String operationName = dto.operationType() == OperationType.ACCRUAL ? "начислено" : "списано";
        return String.format(
                " Операция выполнена!\n" +
                        " %s %s балов\n" +
                        "Новый баланс: %s балов\n" +
                        "Комментарий: %s",
                operationName,
                dto.bonusAmount(),
                newBalance,
                dto.comment() != null ? dto.comment() : DEFAULT_COMMENT
        );
    }
}
