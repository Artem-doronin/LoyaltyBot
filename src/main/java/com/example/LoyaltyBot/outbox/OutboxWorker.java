package com.example.LoyaltyBot.outbox;

import com.example.LoyaltyBot.config.outbox.OutboxProperties;
import com.example.LoyaltyBot.entity.outbox.OutboxMessage;
import com.example.LoyaltyBot.entity.outbox.OutboxMessageStatus;
import com.example.LoyaltyBot.repository.OutboxMessageRepository;
import com.example.LoyaltyBot.service.outbox.OutboxClaimService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxWorker {

    private final BackoffCalculator backoff;
    private final OutboxSender outboxSender;
    private final OutboxClaimService outboxClaimService;
    private final OutboxMessageRepository outboxMessageRepository;
    private final OutboxProperties outboxProperties;

    @Scheduled(fixedDelayString = "${outbox.poll-interval}")
    public void process() {
        List<OutboxMessage> outboxMessageList = outboxClaimService.claim();
        if (outboxMessageList.isEmpty()) return;
        log.debug("Claimed {} messages", outboxMessageList.size());
        for (OutboxMessage m : outboxMessageList) {
            try {
                handle(m);
            } catch (Exception e) {
                log.error("Unexpected error handling id={}", m.getId(), e);
            }
        }
    }

    private void handle(OutboxMessage m) {
        try {
            outboxSender.send(m);
            outboxMessageRepository.markSent(m.getId());
            log.debug("Sent message: id={}, messageId={}", m.getId(), m.getMessageId());
        } catch (Exception e) {
            int nextAttempt = m.getAttempts() + 1;

            String error = e.getClass().getSimpleName()
                    + ": " + (e.getMessage() != null ? e.getMessage() : "no message");

            if (nextAttempt >= outboxProperties.getMaxAttempts()) {
                outboxMessageRepository.markDead(m.getId(), error);
                log.warn("Message marked DEAD, attempts exhausted: id={}, messageId={}, attempts={}, error={}",
                        m.getId(), m.getMessageId(), nextAttempt, error);
            } else {
                Instant nextAt = backoff.nextAttemptAt(nextAttempt);
                outboxMessageRepository.markFailed(m.getId(), OutboxMessageStatus.FAILED.name(), error, nextAt);
                log.warn("Failed to send message: id={}, messageId={}, attempt={}, error={}",
                        m.getId(), m.getMessageId(), nextAttempt, error);
            }
        }
    }
}
