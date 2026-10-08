package com.example.LoyaltyBot.entity.outbox;

public record AttachmentPayload(
        String fileId,
        String fileUrl,
        String type,
        Integer sortOrder
) {}