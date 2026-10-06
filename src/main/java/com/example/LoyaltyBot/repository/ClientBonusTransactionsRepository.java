package com.example.LoyaltyBot.repository;

import com.example.LoyaltyBot.entity.client.ClientBonusTransactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientBonusTransactionsRepository extends JpaRepository<ClientBonusTransactions, Long> {
}
