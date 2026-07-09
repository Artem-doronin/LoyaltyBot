package com.example.LoyaltyBot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
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

    private LocalDate birthday;

    @Column(nullable = false)
    private Integer bonusBalance;

    @Column(nullable = false)
    private Integer totalSpent;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Boolean isActive ;

    public Client() {
    }

    public Client(Long id, Long chatId, String telegramUsername, String firstName, String lastName, String phone,
                  LocalDate birthday, Integer bonusBalance, Integer totalSpent, LocalDateTime createdAt,
                  LocalDateTime updatedAt, Boolean isActive) {
        this.id = id;
        this.chatId = chatId;
        this.telegramUsername = telegramUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.birthday = birthday;
        this.bonusBalance = bonusBalance;
        this.totalSpent = totalSpent;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getTelegramUsername() {
        return telegramUsername;
    }

    public void setTelegramUsername(String telegramUsername) {
        this.telegramUsername = telegramUsername;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Integer getBonusBalance() {
        return bonusBalance;
    }

    public void setBonusBalance(Integer bonusBalance) {
        this.bonusBalance = bonusBalance;
    }

    public Integer getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(Integer totalSpent) {
        this.totalSpent = totalSpent;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setISActive(Boolean active) {
        this.isActive = active;
    }
}