package com.example.LoyaltyBot.service;

import com.example.LoyaltyBot.dto.bonus.BonusOperationDto;
import com.example.LoyaltyBot.entity.OperationType;
import com.example.LoyaltyBot.entity.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
@Slf4j
public class LoyaltyService {
    private final ClientService clientService;
    private final BonusService bonusService;
    private final OperationService operationService;
    private final UserService userService;
    private final NotificationService notificationService;

    public void processBonusOperation(BonusOperationDto dto) {

        if (dto.operationType() == OperationType.ACCRUAL) {
            bonusService.enrollmentBonuses(dto.clientId(), dto.amount());

            log.info("💰 Начислено {} бонусов клиенту {}", dto.amount(), dto.clientId());

        } else if (dto.operationType() == OperationType.WRITE_OFF) {
            bonusService.writeOffBonuses(dto.clientId(), dto.amount());
            log.info("💰 Списано {} бонусов клиенту {}", dto.amount(), dto.clientId());
        }

        User user = userService.getCurrentUser();

        //todo пока не передаю сумму операции а просто бонусы 100 по умолчанию
        operationService.create(new BigDecimal("100"),
                dto.operationType(), dto.amount(),
                dto.comment(), dto.clientId(), user.getId());

        String message = createNotificationMessage(dto,
                bonusService.getAmount(dto.clientId()));
        notificationService.send(dto.clientId(), message);
    }

    private String createNotificationMessage(BonusOperationDto dto, BigDecimal newBalance) {
        String operationName = dto.operationType() == OperationType.ACCRUAL ? "начислено" : "списано";
        return String.format(
                " Операция выполнена!\n" +
                        " %s %s балов\n" +
                        "Новый баланс: %s балов\n" +
                        "Комментарий: %s",
                operationName,
                dto.amount(),
                newBalance,
                dto.comment() != null ? dto.comment() : "Без комментария"
        );
    }
}
