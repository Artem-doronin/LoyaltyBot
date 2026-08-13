package com.example.LoyaltyBot.repository;

import com.example.LoyaltyBot.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation, Long> {
}
