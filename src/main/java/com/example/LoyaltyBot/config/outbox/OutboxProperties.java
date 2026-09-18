package com.example.LoyaltyBot.config.outbox;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
@Validated
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "outbox")
public class OutboxProperties {
    private boolean enabled = false;
    private int batchSize =50;
    private Duration leaseDuration = Duration.ofMinutes(2); //— на сколько секунд воркер «владеет» записью;
    private Duration pollInterval =  Duration.ofSeconds(5); //— как часто запускать воркер;
    private Duration reaperInterval= Duration.ofMinutes(1);// — как часто запускать reaper;
    private int maxAttempts = 5;                           //— после скольких попыток считать сообщение безнадёжным;
    private RetryProperties retry = new RetryProperties(); //— вложенный блок с настройками backoff (base, multiplier, max);
}
