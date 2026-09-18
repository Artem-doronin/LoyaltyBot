package com.example.LoyaltyBot.repository.outbox;

import com.example.LoyaltyBot.entity.outbox.OutboxMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface OutboxMessageRepository extends JpaRepository<OutboxMessage, Long> {

    @Query(value = """
        WITH locked AS (
            SELECT id
            FROM outbox_message
            WHERE status IN ('NEW', 'FAILED')
              AND next_attempt_at <= now()
            ORDER BY next_attempt_at, id
            LIMIT :batchSize
            FOR UPDATE SKIP LOCKED
        )
        UPDATE outbox_message m
        SET status = 'PROCESSING',
            locked_by = :workerId,
            locked_until = :lockedUntil,
            last_attempt_at = now()
        FROM locked
        WHERE m.id = locked.id
        RETURNING m.*
        """, nativeQuery = true)
    List<OutboxMessage> claimBatch(@Param("batchSize") int batchSize,
                                   @Param("workerId") String workerId,
                                   @Param("lockedUntil") Instant lockedUntil);

    @Modifying(clearAutomatically = true)
    @Query(value = """
            UPDATE outbox_message
            SET status = 'SENT',
                sent_at = now(),
                locked_by = NULL,
                locked_until = NULL,
                error_message = NULL
            WHERE id = :id
            """, nativeQuery = true)
    void markSent(@Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query(value = """
            UPDATE outbox_message
            SET status = :status,
                attempts = attempts + 1,
                error_message = :error,
                next_attempt_at = :nextAttemptAt,
                sent_at = NULL,
                locked_by = NULL,
                locked_until = NULL
            WHERE id = :id
            """, nativeQuery = true)
    void markFailed(@Param("id") Long id,
                    @Param("status") String status,
                    @Param("error") String error,
                    @Param("nextAttemptAt") Instant nextAttemptAt);

    @Modifying(clearAutomatically = true)
    @Query(value = """
            UPDATE outbox_message
            SET status = 'NEW',
                locked_by = NULL,
                locked_until = NULL
            WHERE status = 'PROCESSING'
              AND locked_until < now()
            """, nativeQuery = true)
    int releaseStale();
}