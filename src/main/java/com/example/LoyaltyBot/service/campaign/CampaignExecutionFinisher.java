package com.example.LoyaltyBot.service.campaign;

import com.example.LoyaltyBot.entity.campaigns.Campaign;
import com.example.LoyaltyBot.entity.campaigns.CampaignExecution;
import com.example.LoyaltyBot.entity.campaigns.CampaignExecutionStatus;
import com.example.LoyaltyBot.entity.campaigns.CampaignStatus;
import com.example.LoyaltyBot.entity.campaigns.ScheduleType;
import com.example.LoyaltyBot.repository.campaign.CampaignExecutionRepository;
import com.example.LoyaltyBot.repository.campaign.CampaignRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CampaignExecutionFinisher {
    private final CampaignExecutionRepository campaignExecutionRepository;
    private final CampaignRepository campaignRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void finishSuccess(Long executionId,
                              Long campaignId,
                              ScheduleType scheduleType
                              ){

        CampaignExecution execution = campaignExecutionRepository.findById(executionId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "CampaignExecution not found: " + executionId));

        execution.setStatus(CampaignExecutionStatus.DONE);
        execution.setFinishedAt(Instant.now());


        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Campaign not found: " + campaignId));

        campaign.setStatus(resolveNextStatus(scheduleType,true));

    }
@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void finishFailure(Long executionId, Long campaignId,
                              ScheduleType scheduleType, String errorMessage){
    CampaignExecution execution = campaignExecutionRepository.findById(executionId)
            .orElseThrow(() -> new EntityNotFoundException(
                    "CampaignExecution not found: " + executionId));

    execution.setStatus(CampaignExecutionStatus.FAILED);
    execution.setFinishedAt(Instant.now());
    execution.setErrorMessage(errorMessage);


    Campaign campaign = campaignRepository.findById(campaignId)
            .orElseThrow(() -> new EntityNotFoundException(
                    "Campaign not found: " + campaignId));

    campaign.setStatus(resolveNextStatus(scheduleType,false));


    }
    private CampaignStatus resolveNextStatus(ScheduleType scheduleType, boolean success) {
        if (scheduleType == ScheduleType.RECURRING) {
            return CampaignStatus.SCHEDULED;
        }
        return success ? CampaignStatus.FINISHED : CampaignStatus.FAILED;
    }

}
