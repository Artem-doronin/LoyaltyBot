package com.example.LoyaltyBot.repository;

import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.entity.RegistrationState;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByTelegramUserId(Long telegramId);

    @Query(value = """
                SELECT * FROM clients c
                WHERE c.phone LIKE CONCAT('%', :query, '%')
                ORDER BY c.first_name ASC
                LIMIT :limit
            """, nativeQuery = true)
    List<Client> searchByPhoneWithLimit(
            @Param("query") String query,
            @Param("limit") int limit);

//todo (:afterId is null or c.id > :afterId) — потенциальная проблема производительности
    @Query("""
    select c from Client c
    where c.isActive = true
      and c.registrationState = :state
      and (:afterId is null or c.id > :afterId)
    order by c.id asc
""")
    List<Client> findBatchByRegistrationState(
            @Param("state") RegistrationState state,
            @Param("afterId") Long afterId,
            Pageable pageable);

    default List<Client> findActiveRegisteredBatch(Long afterId, Pageable pageable) {
        return findBatchByRegistrationState(RegistrationState.REGISTERED, afterId, pageable);
    }

    @Query("""
    select c from Client c
    where c.isActive = true
      and c.registrationState = :state
      and c.birthday is not null
      and extract(month from c.birthday) = :month
      and extract(day from c.birthday) = :day
      and (:afterId is null or c.id > :afterId)
    order by c.id asc
""")
    List<Client> findBirthdayBatch(
            @Param("state") RegistrationState state,
            @Param("month") int month,
            @Param("day") int day,
            @Param("afterId") Long afterId,
            Pageable pageable);

    default List<Client> findActiveRegisteredBirthdayBatch(
            int month, int day, Long afterId, Pageable pageable) {
        return findBirthdayBatch(RegistrationState.REGISTERED, month, day, afterId, pageable);
    }

    @Query(value = """
                SELECT c.* FROM clients c
                           JOIN client_bonus_balances cbb on c.id = cbb.client_id
                WHERE c.is_active = true
                  AND c.registration_state = :state
                  AND cbb.amount > :minAmount
                  AND (:afterId is null or c.id > :afterId)
                ORDER BY c.id
            """, nativeQuery = true)
    List<Client> findWithBonusGreaterThan(
            @Param("state") RegistrationState state,
            @Param("minAmount") BigDecimal minAmount,
            @Param("afterId") Long afterId,
            Pageable pageable);

    default List<Client> findActiveRegisteredWithBonusGreaterThan(
            BigDecimal minAmount, Long afterId, Pageable pageable) {
        return findWithBonusGreaterThan(RegistrationState.REGISTERED, minAmount, afterId, pageable);
    }

}


