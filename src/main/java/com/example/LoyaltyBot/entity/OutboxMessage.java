package com.example.LoyaltyBot.entity;

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
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Setter
@Table(name = "outbox_message")
@Entity
public class OutboxMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long clientId;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OutboxMessageStatus status;
    private Integer attempts = 0;
    @CreationTimestamp
    private Instant createdAt;
    private Instant nextAttemptAt;
    private String errorMessage;
}
