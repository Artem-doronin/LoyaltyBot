package com.example.LoyaltyBot.outbox;

import com.example.LoyaltyBot.config.outbox.OutboxProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@RequiredArgsConstructor
@Component
public class BackoffCalculator {
    private final OutboxProperties outboxProperties;

    public Instant nextAttemptAt(int attempts) {
        if (attempts <= 0) {
            throw new IllegalArgumentException("attempts must be > 0, got: " + attempts);
        }

        long baseMillis = outboxProperties.getRetry().getBase().toMillis();
        long maxMillis = outboxProperties.getRetry().getMax().toMillis();

        long delayMillis = (long) (baseMillis * Math.pow(outboxProperties.getRetry().getMultiplier(), attempts - 1));
        if (delayMillis > maxMillis) delayMillis = maxMillis;
        Duration delay = Duration.ofMillis(delayMillis);

        return Instant.now().plus(delay);
    }
}
