package com.example.LoyaltyBot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(nullable = false)
    private Integer amount;  // +50 или -30

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private BigDecimal purchaseAmount;  // сумма покупки в рублях

    private String description;

    private String receiptId;  // ID чека

    @Enumerated(EnumType.STRING)
    private TransactionSource source;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

}