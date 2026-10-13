package com.example.LoyaltyBot.service.campaign;

import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.entity.campaigns.AudienceParams;
import com.example.LoyaltyBot.entity.campaigns.AudienceType;
import com.example.LoyaltyBot.entity.campaigns.Campaign;
import com.example.LoyaltyBot.entity.campaigns.CampaignAttachment;
import com.example.LoyaltyBot.entity.campaigns.CampaignExecution;
import com.example.LoyaltyBot.entity.campaigns.CampaignMessage;
import com.example.LoyaltyBot.entity.outbox.AttachmentPayload;
import com.example.LoyaltyBot.repository.campaign.CampaignAttachmentRepository;
import com.example.LoyaltyBot.repository.campaign.CampaignMessageRepository;
import com.example.LoyaltyBot.repository.campaign.CampaignRepository;
import com.example.LoyaltyBot.service.resolver.AudienceResolver;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CampaignService {
    private final CampaignExecutionStarter starter;
    private final CampaignRepository campaignRepository;
    private final CampaignMessageRepository campaignMessageRepository;
    private final CampaignAttachmentRepository campaignAttachmentRepository;
    private final List<AudienceResolver> audienceResolvers;
    private final CampaignBatchProcessor campaignBatchProcessor;
    private final CampaignExecutionUpdater campaignExecutionUpdater;
    private final CampaignExecutionFinisher executionFinisher;

    @Value("${campaign.bath-size}")
    private int batchSize;


    public void execute(Long campaignId, Instant scheduledAt){
        Optional<CampaignExecution> started = starter.start(campaignId, scheduledAt);
        if (started.isEmpty()) {
            log.info("Campaign {} not started for scheduledAt={}", campaignId, scheduledAt);
            return;
        }
        CampaignExecution execution = started.get();
        Long executionId = execution.getId();

        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new EntityNotFoundException("Campaign not found: " + campaignId));

        Optional<CampaignMessage> campaignMessageOpt = campaignMessageRepository.findByCampaignId(campaignId);

        if (campaignMessageOpt.isEmpty()) {
            executionFinisher.finishFailure(executionId,campaignId, campaign.getScheduleType(),
                    "No CampaignMessage for campaign " + campaignId);
            return;
        }
        CampaignMessage campaignMessage = campaignMessageOpt.get();
        String text = campaignMessage.getText();

        List<AttachmentPayload> attachments = loadAttachments(campaignMessage.getId());

        AudienceResolver audienceResolver = buildResolverMap().get(campaign.getAudienceType());

        if (audienceResolver == null) {
            executionFinisher.finishFailure(executionId,campaignId, campaign.getScheduleType(),
                    "No resolver for audienceType " + campaign.getAudienceType());
            return;
        }

        AudienceParams params = campaign.getAudienceParams();

        Long afterId = null;
        try {
            while (true) {
                List<Client> batch = audienceResolver.resolveBatch(params, afterId, batchSize);
                if (batch.isEmpty()) {
                    break;
                }

                int enqueued = campaignBatchProcessor.process(batch, text, executionId, attachments);
                int failed = batch.size() - enqueued;

                campaignExecutionUpdater.addCounts(executionId, batch.size(), enqueued, failed);

                if (batch.size() < batchSize) {
                    break;
                }

                afterId = batch.get(batch.size() - 1).getId();
            }

            executionFinisher.finishSuccess(executionId, campaignId, campaign.getScheduleType());


        } catch (Exception e) {
            log.error("Campaign {} execution {} failed", campaignId, executionId, e);
            executionFinisher.finishFailure(executionId,campaignId, campaign.getScheduleType(), e.getMessage());
        }


    }
    private List<AttachmentPayload> loadAttachments(Long campaignMessageId) {
        List<CampaignAttachment> attachments = campaignAttachmentRepository
                .findByCampaignMessageIdOrderBySortOrderAsc(campaignMessageId);

        return attachments.stream()
                .map(a -> new AttachmentPayload(
                        a.getFileId(),
                        a.getFileUrl(),
                        a.getType(),
                        a.getSortOrder()))
                .toList();
    }

    private Map<AudienceType, AudienceResolver> buildResolverMap() {
        return audienceResolvers.stream()
                .collect(Collectors.toMap(AudienceResolver::supports, Function.identity()));
    }
}
