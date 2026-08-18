package com.example.LoyaltyBot.repository;

import com.example.LoyaltyBot.entity.Bonus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BonusRepository extends JpaRepository<Bonus, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Bonus b WHERE b.clientId = :clientId")
    Optional<Bonus> findByClientIdWithLock(@Param("clientId") Long clientId);
}
