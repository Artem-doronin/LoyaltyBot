package com.example.LoyaltyBot.config.outbox;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "outbox")
public class OutboxProperties {
    private boolean enabled = false;
    private int batchSize = 50;
    private Duration leaseDuration = Duration.ofMinutes(2);
    private Duration pollInterval =  Duration.ofSeconds(5);
    private Duration reaperInterval= Duration.ofMinutes(1);
    private int maxAttempts = 5;
    private RetryProperties retry = new RetryProperties();
}
