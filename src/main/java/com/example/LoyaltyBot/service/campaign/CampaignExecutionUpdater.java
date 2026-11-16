package com.example.LoyaltyBot.service.campaign;

import com.example.LoyaltyBot.repository.campaign.CampaignExecutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CampaignExecutionUpdater {
    private final CampaignExecutionRepository campaignExecutionRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addCounts(Long executionId, int recipients, int enqueued, int failed) {
        campaignExecutionRepository.addCounts(executionId, recipients, enqueued, failed);
    }
}
