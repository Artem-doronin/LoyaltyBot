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

    Optional<Client> findByPhone(String phoneNumber);

    @Query(value = """
                SELECT * FROM clients c
                WHERE c.phone LIKE CONCAT('%', :query, '%')
                ORDER BY c.first_name ASC
                LIMIT :limit
            """, nativeQuery = true)
    List<Client> searchByPhoneWithLimit(
            @Param("query") String query,
            @Param("limit") int limit);
}


