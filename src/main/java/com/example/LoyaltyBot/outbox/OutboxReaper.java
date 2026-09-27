package com.example.LoyaltyBot.outbox;

import com.example.LoyaltyBot.repository.OutboxMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class OutboxReaper {
    private final OutboxMessageRepository repository;

    @Transactional
    @Scheduled(fixedDelayString = "${outbox.reaper-interval}")
    public void reap() {
        int released = repository.releaseStale();
        if (released > 0) {
            log.warn("Released {} stale PROCESSING messages", released);
        }
    }
}
