package com.example.LoyaltyBot.service.campaign;

import com.example.LoyaltyBot.entity.campaigns.CampaignExecution;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CampaignService {
    private final CampaignExecutionStarter starter;


    public void execute(Long campaignId, Instant scheduledAt){
        Optional<CampaignExecution> execution = starter.start(campaignId,scheduledAt);
        if(execution.isEmpty())return;

    }
}
