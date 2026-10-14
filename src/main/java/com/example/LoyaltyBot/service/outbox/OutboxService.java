package com.example.LoyaltyBot.service.outbox;

import com.example.LoyaltyBot.entity.outbox.AttachmentPayload;
import com.example.LoyaltyBot.entity.outbox.OutboxMessage;
import com.example.LoyaltyBot.repository.OutboxMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OutboxService {
    private final OutboxMessageRepository outboxMessageRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void enqueue(Long clientId, String text) {
        outboxMessageRepository.save(OutboxMessage.create(clientId, text));
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void enqueueFromCampaign(
            Long clientId, String payload,
            Long campaignExecutionId, List<AttachmentPayload> attachmentPayloadList) {
        outboxMessageRepository.save(
                OutboxMessage.create(clientId, payload, campaignExecutionId,attachmentPayloadList));
    }
}
