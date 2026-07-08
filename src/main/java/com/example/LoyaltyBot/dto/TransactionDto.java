package com.example.LoyaltyBot.dto;

import com.example.LoyaltyBot.entity.Transaction;
import com.example.LoyaltyBot.entity.TransactionSource;
import com.example.LoyaltyBot.entity.TransactionType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record TransactionDto(
        Long id,
        Integer amount,
        TransactionType type,
        BigDecimal purchaseAmount,
        String description,
        String receiptId,
        TransactionSource source,
        LocalDateTime createdAt
) {
    public static TransactionDto toDto(Transaction transaction) {
        return TransactionDto.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .purchaseAmount(transaction.getPurchaseAmount())
                .description(transaction.getDescription())
                .receiptId(transaction.getReceiptId())
                .source(transaction.getSource())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}