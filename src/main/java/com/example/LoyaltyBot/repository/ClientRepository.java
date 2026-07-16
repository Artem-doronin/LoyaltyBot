package com.example.LoyaltyBot.repository;

import com.example.LoyaltyBot.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByChatId(Long chatId);
}
