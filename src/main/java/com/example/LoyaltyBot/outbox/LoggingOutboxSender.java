package com.example.LoyaltyBot.outbox;

import com.example.LoyaltyBot.entity.outbox.OutboxMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ConditionalOnProperty(name = "outbox.sender.type", havingValue = "log", matchIfMissing = true)
public class LoggingOutboxSender implements OutboxSender {
    @Override
    public void send(OutboxMessage outboxMessage) {
        log.debug("Sent outbox message: messageId={}, clientId={}",
                outboxMessage.getMessageId(), outboxMessage.getClientId());
    }
}
