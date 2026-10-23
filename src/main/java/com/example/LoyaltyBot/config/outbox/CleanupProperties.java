package com.example.LoyaltyBot.config.outbox;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CleanupProperties {
    private boolean enabled = false;
    private String cron = "0 0 3 * * *";
    private int retentionDays = 30;
}
