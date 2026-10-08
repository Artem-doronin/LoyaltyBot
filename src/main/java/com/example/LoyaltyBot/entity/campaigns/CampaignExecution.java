package com.example.LoyaltyBot.entity.campaigns;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "campaign_execution")
public class CampaignExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long campaignId;
    private Instant scheduledAt;
    private Instant startedAt;
    private Instant finishedAt;

    @Enumerated(EnumType.STRING)
    private CampaignExecutionStatus status;
    private Integer recipientsCount;
    private Integer enqueuedCount;
    private Integer failedCount;
    @Column(columnDefinition = "TEXT")
    private String errorMessage;

}
