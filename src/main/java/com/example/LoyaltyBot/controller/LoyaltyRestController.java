package com.example.LoyaltyBot.controller;

import com.example.LoyaltyBot.dto.SuccessBonusResponse;
import com.example.LoyaltyBot.dto.SuccessTransactionResponseDto;
import com.example.LoyaltyBot.dto.bonus.BonusResponseDto;
import com.example.LoyaltyBot.dto.bonus.ClientBonusTransactionDto;
import com.example.LoyaltyBot.dto.client.ClientResponseDto;
import com.example.LoyaltyBot.dto.client.ClientResponseSearchDto;
import com.example.LoyaltyBot.service.ClientBonusBalancesService;
import com.example.LoyaltyBot.service.ClientBonusTransactionsService;
import com.example.LoyaltyBot.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoyaltyRestController {
    private final ClientService clientService;
    private final ClientBonusTransactionsService clientBonusTransactionsService;
    private final ClientBonusBalancesService clientBonusBalancesService;


    @GetMapping("/search")
    public List<ClientResponseSearchDto> searchClients(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit) {
        log.debug("AJAX поиск клиентов по запросу: {}", query);
        if (query == null || query.trim().isEmpty()) {
            return List.of();
        }
        List<ClientResponseSearchDto> results = clientService.searchClients(query);
        if (limit > 0 && results.size() > limit) {
            return results.subList(0, limit);
        }
        return results;
    }

    @GetMapping("/find")
    public SuccessBonusResponse findClientAjax(@RequestParam String phoneNumber) {
        log.info("AJAX поиск клиента по номеру: {}", phoneNumber);

        String normalizedPhone = phoneNumber.trim();
        ClientResponseDto dto = clientService.findByPhoneNumber(normalizedPhone);
        BonusResponseDto bonusResponseDto = clientBonusBalancesService.getBonusDto(dto.id());

        SuccessBonusResponse successBonusResponse = SuccessBonusResponse.builder()
                .message("Клиент найден!")
                .id(dto.id())
                .firstName(dto.firstName())
                .phone(dto.phone())
                .bonusAmount(bonusResponseDto.amount())
                .rate(bonusResponseDto.rate())
                .build();

        log.info("Клиент найден: ID={}, Имя={}, Бонусы={}",
                dto.id(), dto.firstName(), bonusResponseDto.amount());

        return successBonusResponse;
    }

    @PostMapping("/enroll")
    public SuccessTransactionResponseDto enroll(@RequestBody ClientBonusTransactionDto transactionDto) {
        log.info("📝 AJAX операция: {} для клиента {}",
                transactionDto.operationType(), transactionDto.clientId());
        BigDecimal newBalance = clientBonusTransactionsService.enroll(transactionDto);
        return SuccessTransactionResponseDto.builder()
                .newBalance(newBalance)
                .message(String.format("Начислено %s бонусов!", transactionDto.bonusAmount()))
                .clientId(transactionDto.clientId())
                .operationAmount(transactionDto.operationAmount())
                .amount(transactionDto.bonusAmount())
                .build();

    }

    @PostMapping("/writeOff")
    public SuccessTransactionResponseDto writeOff(@RequestBody ClientBonusTransactionDto transactionDto) {
        log.info("📝 AJAX операция: {} для клиента {}",
                transactionDto.operationType(), transactionDto.clientId());
        BigDecimal newBalance = clientBonusTransactionsService.writeOff(transactionDto);
        return SuccessTransactionResponseDto.builder()
                .newBalance(newBalance)
                .message(String.format(String.format("Списано %s бонусов!", transactionDto.bonusAmount())))
                .clientId(transactionDto.clientId())
                .operationAmount(transactionDto.operationAmount())
                .amount(transactionDto.bonusAmount())
                .build();
    }
}
