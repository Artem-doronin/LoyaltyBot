package com.example.LoyaltyBot.outbox;


import com.example.LoyaltyBot.config.outbox.OutboxProperties;
import com.example.LoyaltyBot.repository.OutboxMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "outbox.cleanup.enabled", havingValue = "true")
public class OutboxCleaner {
    private final OutboxProperties outboxProperties;
    private final OutboxMessageRepository repository;

    @Scheduled(cron = "${outbox.cleanup.cron}")
    public void cleanup(){
        int deleted = repository.deleteOld(outboxProperties.getCleanup().getRetentionDays());
        if (deleted > 0) log.info("Deleted {} old outbox messages", deleted);
    }
}
