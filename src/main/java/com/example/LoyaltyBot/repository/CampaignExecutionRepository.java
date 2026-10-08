package com.example.LoyaltyBot.repository;

import com.example.LoyaltyBot.entity.campaigns.CampaignExecution;
import com.example.LoyaltyBot.entity.campaigns.CampaignExecutionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
