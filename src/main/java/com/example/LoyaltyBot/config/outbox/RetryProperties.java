package com.example.LoyaltyBot.config.outbox;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
@Getter
@Setter
public class RetryProperties {
    private Duration base = Duration.ofMinutes(1);
    private double multiplier = 2.0;
    private Duration max= Duration.ofHours(1);
}
