package com.example.LoyaltyBot.entity.outbox;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Table(name = "outbox_message")
@Entity
@NoArgsConstructor
public class OutboxMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long clientId;
    @Column(nullable = false, unique = true)
    private UUID messageId;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OutboxMessageStatus status;
    private String lockedBy;
    private Instant lockedUntil;
    @Column(nullable = false)
    private Integer attempts = 0;
    @CreationTimestamp
    private Instant createdAt;
    private Instant nextAttemptAt;
    @Column(columnDefinition = "TEXT")
    private String errorMessage;
    private Long campaignExecutionId;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<AttachmentPayload> attachments;


    public static OutboxMessage create(Long clientId, String payload) {
        return create(clientId, payload, null, null);
    }

    public static OutboxMessage create(Long clientId, String payload, Long campaignExecutionId) {
        return create(clientId, payload, campaignExecutionId, null);
    }

    public static OutboxMessage create(Long clientId, String payload,
                                       Long campaignExecutionId,
                                       List<AttachmentPayload> attachments) {
        OutboxMessage m = new OutboxMessage();
        m.clientId = clientId;
        m.payload = payload;
        m.campaignExecutionId = campaignExecutionId;
        m.attachments = attachments;
        m.messageId = UUID.randomUUID();
        m.status = OutboxMessageStatus.NEW;
        m.attempts = 0;
        m.nextAttemptAt = Instant.now();
        return m;
    }
}
