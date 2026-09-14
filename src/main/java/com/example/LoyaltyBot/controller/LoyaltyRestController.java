package com.example.LoyaltyBot.controller;

import com.example.LoyaltyBot.dto.SuccessClientResponse;
import com.example.LoyaltyBot.dto.SuccessTransactionResponseDto;
import com.example.LoyaltyBot.dto.bonus.ClientBonusTransactionDto;
import com.example.LoyaltyBot.dto.client.ClientResponseSearchDto;
import com.example.LoyaltyBot.service.ClientBonusTransactionsService;
import com.example.LoyaltyBot.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.index.qual.Positive;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/loyalty")
@RequiredArgsConstructor
public class LoyaltyRestController {
    private final ClientService clientService;
    private final ClientBonusTransactionsService clientBonusTransactionsService;

    @GetMapping("/search")
    public List<ClientResponseSearchDto> searchByPhone(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") @Positive int limit) {
        return clientService.searchByPhone(query, limit);
    }

    @GetMapping("/find")
    public SuccessClientResponse findClientAjax(@RequestParam String phoneNumber) {
        return clientService.findByPhoneNumber(phoneNumber);
    }

    @PostMapping("/enroll")
    public SuccessTransactionResponseDto enroll(@RequestBody ClientBonusTransactionDto transactionDto) {
        return clientBonusTransactionsService.enroll(transactionDto);
    }

    @PostMapping("/writeOff")
    public SuccessTransactionResponseDto writeOff(@RequestBody ClientBonusTransactionDto transactionDto) {
        return clientBonusTransactionsService.writeOff(transactionDto);
    }
}