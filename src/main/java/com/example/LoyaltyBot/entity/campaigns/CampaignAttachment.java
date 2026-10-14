package com.example.LoyaltyBot.entity.campaigns;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "campaign_attachments")
public class CampaignAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long campaignMessageId;
    private String fileUrl;
    private String fileId;
    @Enumerated(EnumType.STRING)
    private AttachmentType type;
    private Integer sortOrder;
}
