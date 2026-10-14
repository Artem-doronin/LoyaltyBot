package com.example.LoyaltyBot.service.campaign;

import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.entity.outbox.AttachmentPayload;
import com.example.LoyaltyBot.repository.OutboxMessageRepository;
import com.example.LoyaltyBot.service.outbox.OutboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CampaignBatchProcessor {
    private final OutboxService outboxService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int process(
            List<Client> batch,
            String text,
            Long executionId,
            List<AttachmentPayload> attachments
    ) {
        for (Client client : batch) {
            outboxService.enqueueFromCampaign(client.getId(),
                    text, executionId, attachments);
        }
        return batch.size();
    }
}
