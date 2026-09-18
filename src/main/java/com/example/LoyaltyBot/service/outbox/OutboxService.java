package com.example.LoyaltyBot.service.outbox;

import com.example.LoyaltyBot.entity.outbox.OutboxMessage;
import com.example.LoyaltyBot.repository.outbox.OutboxMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OutboxService {
    private final OutboxMessageRepository outboxMessageRepository;
    public void enqueue(Long clientId,String text){
        outboxMessageRepository.save(OutboxMessage.create(clientId,text));
    }
}
