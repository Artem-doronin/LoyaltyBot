package com.example.LoyaltyBot.service;

import com.example.LoyaltyBot.dto.TransactionDto;
import com.example.LoyaltyBot.entity.Transaction;
import com.example.LoyaltyBot.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class TransactionService {
    private TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public TransactionDto findById(long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(
                ()->new RuntimeException("Transaction not found"));
        return TransactionDto.toDto(transaction);
    }

    public List<TransactionDto> findByClientId(Long clientId) {
        List<Transaction> transactions = transactionRepository.findByClientId(clientId).orElseThrow(
                ()->new RuntimeException("Transaction not found"));
        return transactions.stream()
                .map(TransactionDto::toDto)
                .toList();


    }
}
