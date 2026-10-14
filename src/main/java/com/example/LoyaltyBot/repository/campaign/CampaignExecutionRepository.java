package com.example.LoyaltyBot.repository.campaign;

import com.example.LoyaltyBot.entity.campaigns.CampaignExecution;
import com.example.LoyaltyBot.entity.campaigns.CampaignExecutionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface CampaignExecutionRepository extends JpaRepository<CampaignExecution, Long> {

    Optional<CampaignExecution> findByCampaignIdAndScheduledAt(Long campaignId, Instant scheduledAt);

    List<CampaignExecution> findByCampaignIdAndStatus(Long campaignId, CampaignExecutionStatus status);

    Optional<CampaignExecution> findTopByCampaignIdOrderByScheduledAtDesc(Long campaignId);

    List<CampaignExecution> findByStatus(CampaignExecutionStatus status);

    boolean existsByCampaignIdAndScheduledAt(Long campaignId, Instant scheduledAt);

    @Modifying(clearAutomatically = true)
    @Query("""
    update CampaignExecution e
    set e.recipientsCount = coalesce(e.recipientsCount, 0) + :recipients,
        e.enqueuedCount   = coalesce(e.enqueuedCount, 0) + :enqueued,
        e.failedCount     = coalesce(e.failedCount, 0) + :failed
    where e.id = :id
""")
    void addCounts(@Param("id") Long id,
                   @Param("recipients") int recipients,
                   @Param("enqueued") int enqueued,
                   @Param("failed") int failed);
}
