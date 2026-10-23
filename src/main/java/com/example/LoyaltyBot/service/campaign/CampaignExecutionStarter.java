package com.example.LoyaltyBot.service.campaign;

import com.example.LoyaltyBot.entity.campaigns.Campaign;
import com.example.LoyaltyBot.entity.campaigns.CampaignExecution;
import com.example.LoyaltyBot.entity.campaigns.CampaignExecutionStatus;
import com.example.LoyaltyBot.entity.campaigns.CampaignMessage;
import com.example.LoyaltyBot.entity.campaigns.CampaignStatus;
import com.example.LoyaltyBot.repository.campaign.CampaignAttachmentRepository;
import com.example.LoyaltyBot.repository.campaign.CampaignExecutionRepository;
import com.example.LoyaltyBot.repository.campaign.CampaignMessageRepository;
import com.example.LoyaltyBot.repository.campaign.CampaignRepository;
import com.example.LoyaltyBot.util.AudienceParamsValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CampaignExecutionStarter {
    private final CampaignRepository campaignRepository;
    private final CampaignExecutionRepository campaignExecutionRepository;


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Optional<CampaignExecution> start(Long campaignId, Instant scheduledAt) {
        Campaign campaign = campaignRepository.findById(campaignId).orElseThrow(
                () -> new EntityNotFoundException("Campaign not found: " + campaignId));

        if (campaign.getStatus() != CampaignStatus.SCHEDULED)
            return Optional.empty();

        markStuckExecutionsAsFailed(campaignId);

        if (campaignExecutionRepository.existsByCampaignIdAndScheduledAt(campaignId, scheduledAt)) {
            return Optional.empty();
        }

        try {
            CampaignExecution execution = CampaignExecution.create(campaignId, scheduledAt);
            campaignExecutionRepository.save(execution);

            campaign.setStatus(CampaignStatus.ACTIVE);
            campaignRepository.save(campaign);

            return Optional.of(execution);
        } catch (DataIntegrityViolationException | OptimisticLockingFailureException e) {
            return Optional.empty();
        }
    }


    private void markStuckExecutionsAsFailed(Long campaignId) {
        List<CampaignExecution> stuck = campaignExecutionRepository
                .findByCampaignIdAndStatus(campaignId, CampaignExecutionStatus.RUNNING);

        if (stuck.isEmpty()) {
            return;
        }

        Instant now = Instant.now();
        for (CampaignExecution execution : stuck) {
            execution.setStatus(CampaignExecutionStatus.FAILED);
            execution.setFinishedAt(now);
            execution.setErrorMessage("Marked as failed on new start");
        }
    }
}
