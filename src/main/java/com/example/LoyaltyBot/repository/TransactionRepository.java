package com.example.LoyaltyBot.repository;

import com.example.LoyaltyBot.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<List<Transaction>> findByClientId(Long client_id);

}
