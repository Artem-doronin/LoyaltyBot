package com.example.LoyaltyBot.repository;

import com.example.LoyaltyBot.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByTelegramUserId(Long telegramId);

    @Query("SELECT c FROM Client c WHERE c.phone LIKE %:query%")
    List<Client> searchByPhoneNumber(@Param("query") String query);

    Optional<Client> findByPhone(String phoneNumber);
}
