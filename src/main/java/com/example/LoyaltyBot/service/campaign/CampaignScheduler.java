package com.example.LoyaltyBot.service.campaign;

import com.example.LoyaltyBot.entity.campaigns.Campaign;
import com.example.LoyaltyBot.entity.campaigns.CampaignExecution;
import com.example.LoyaltyBot.entity.campaigns.CampaignStatus;
import com.example.LoyaltyBot.entity.campaigns.ScheduleType;
import com.example.LoyaltyBot.repository.campaign.CampaignExecutionRepository;
import com.example.LoyaltyBot.repository.campaign.CampaignRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.time.ZoneId;


@Component
@RequiredArgsConstructor
@Slf4j
public class CampaignScheduler {

    private static final ZoneId CRON_ZONE = ZoneId.of("Europe/Moscow");

    private final CampaignRepository campaignRepository;
    private final CampaignExecutionRepository executionRepository;
    private final CampaignService campaignService;

    @Scheduled(fixedDelay = 60_000, initialDelay = 30_000)
    public void tick() {
        List<Campaign> scheduled = campaignRepository.findByStatus(CampaignStatus.SCHEDULED);
        if (scheduled.isEmpty()) {
            return;
        }
        log.debug("CampaignScheduler: {} scheduled campaigns to check", scheduled.size());

        for (Campaign campaign : scheduled) {
            try {


                if (campaign.getScheduleType() == ScheduleType.ONE_TIME) {
                    handleOneTime(campaign);
                } else {
                    handleRecurring(campaign);
                }
            } catch (Exception e) {
                log.error("CampaignScheduler: failed to process campaign {}", campaign.getId(), e);
            }

        }
    }

    private void handleOneTime(Campaign campaign) {
        Instant sendAt = campaign.getSendAt();
        if (sendAt == null) {
            log.warn("Campaign {} has no sendAt for ONE_TIME", campaign.getId());
            return;
        }

        if (sendAt.isAfter(Instant.now())) {
            return;
        }

        log.info("CampaignScheduler: starting ONE_TIME campaign {} at {}", campaign.getId(), sendAt);
        campaignService.execute(campaign.getId(), sendAt);
    }

    private void handleRecurring(Campaign campaign) {
        Instant anchor = executionRepository
                .findTopByCampaignIdOrderByScheduledAtDesc(campaign.getId())
                .map(CampaignExecution::getScheduledAt)
                .orElse(campaign.getCreatedAt());

        Instant next = resolveNextFireTime(campaign, anchor);
        if (next == null) {
            log.warn("Campaign {} has no next fire time for cron '{}'",
                    campaign.getId(), campaign.getCronExpression());
            return;
        }

        if (next.isAfter(Instant.now())) {
            return;
        }

        log.info("CampaignScheduler: starting RECURRING campaign {} at {}", campaign.getId(), next);
        campaignService.execute(campaign.getId(), next);
    }
    private Instant resolveNextFireTime(Campaign campaign, Instant anchor) {
        String cron = campaign.getCronExpression();
        if (cron == null || cron.isBlank()) {
            return null;
        }

        CronExpression expression = CronExpression.parse(normalizeCron(cron));
        ZonedDateTime nextZoned = expression.next(anchor.atZone(CRON_ZONE));
        return nextZoned != null ? nextZoned.toInstant() : null;
    }

    private String normalizeCron(String cron) {
        String trimmed = cron.trim();
        long fields = trimmed.split("\\s+").length;
        return fields == 5 ? "0 " + trimmed : trimmed;
    }
}
