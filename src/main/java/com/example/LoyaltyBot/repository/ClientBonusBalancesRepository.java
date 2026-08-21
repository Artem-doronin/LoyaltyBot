package com.example.LoyaltyBot.repository;

import com.example.LoyaltyBot.entity.ClientBonusBalances;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ClientBonusBalancesRepository extends JpaRepository<ClientBonusBalances, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM ClientBonusBalances c WHERE c.clientId = :clientId")
    Optional<ClientBonusBalances> findByClientIdWithLock(@Param("clientId") Long clientId);

    @Query("SELECT c.amount FROM ClientBonusBalances c WHERE c.clientId = :clientId")
    Optional<BigDecimal> findAmountByClientId(@Param("clientId") Long clientId);
    Optional<ClientBonusBalances> findByClientId(Long clientId);
}
