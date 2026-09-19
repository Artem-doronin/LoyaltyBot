package com.example.LoyaltyBot.outbox;

import com.example.LoyaltyBot.entity.outbox.OutboxMessage;

public interface OutboxSender {
    void send(OutboxMessage outboxMessage);
}
