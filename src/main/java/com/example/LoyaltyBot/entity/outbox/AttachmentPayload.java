package com.example.LoyaltyBot.entity.outbox;

import com.example.LoyaltyBot.entity.campaigns.AttachmentType;

public record AttachmentPayload(
        String fileId,
        String fileUrl,
        AttachmentType type,
        Integer sortOrder
) {}