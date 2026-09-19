package com.example.LoyaltyBot.service.outbox;

import com.example.LoyaltyBot.config.outbox.OutboxProperties;
import com.example.LoyaltyBot.entity.outbox.OutboxMessage;
import com.example.LoyaltyBot.repository.OutboxMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxClaimService {
    private final OutboxMessageRepository outboxMessageRepository;
    private final OutboxProperties outboxProperties;

    @Transactional
    public List<OutboxMessage> claim() {
        if (!outboxProperties.isEnabled()) {
            return Collections.emptyList();
        }

        Instant lockedUntil = Instant.now().plus(outboxProperties.getLeaseDuration());
        return outboxMessageRepository.claimBatch(outboxProperties.getBatchSize(), getWorkerId(), lockedUntil);
    }

    private String getWorkerId() {
        String host;
        try {
            host = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            log.warn("Failed to resolve hostname, using 'unknown' as workerId", e);
            host = "unknown";
        }
        long pid = ProcessHandle.current().pid();
        String threadName = Thread.currentThread().getName();
        return String.format("%s:%d:%s", host, pid, threadName);
    }
}
