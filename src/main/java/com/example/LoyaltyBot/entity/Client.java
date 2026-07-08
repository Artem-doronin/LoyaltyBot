package com.example.LoyaltyBot.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long chatId;

    @Column(length = 50)
    private String telegramUsername;

    @Column(length = 50)
    private String firstName;

    @Column(length = 50)
    private String lastName;

    @Column(length = 20)
    private String phone;

    private LocalDateTime birthday;

    @Column(nullable = false)
    private Integer bonusBalance = 0;

    @Column(nullable = false)
    private Integer totalSpent = 0;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime registeredAt;

    @UpdateTimestamp
    private LocalDateTime lastActiveAt;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false)
    private String role = "CLIENT";

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();

    public Client() {
    }

    public Client(Long chatId, String firstName, String lastName, String telegramUsername) {
        this.chatId = chatId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.telegramUsername = telegramUsername;
        this.registeredAt = LocalDateTime.now();
        this.lastActiveAt = LocalDateTime.now();
        this.bonusBalance = 0;
        this.totalSpent = 0;
        this.active = true;
        this.role = "CLIENT";
    }

    // Вычисляемые поля
    @Transient
    public String getLoyaltyLevel() {
        if (bonusBalance >= 1000) return "GOLD";
        if (bonusBalance >= 500) return "SILVER";
        return "BRONZE";
    }

    @Transient
    public String getFullName() {
        return (firstName != null ? firstName : "") +
                (lastName != null ? " " + lastName : "");
    }
}