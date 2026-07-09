package com.example.LoyaltyBot.dto.client;

import java.time.LocalDate;

public class ClientResponseDto extends ClientBaseDto {
    private Long id;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private Boolean isActive;
    private Integer totalSpent;
    private Integer bonusBalance;

    public ClientResponseDto(String firstName, String lastName, LocalDate birthday, String chatId, String phone,
                             String telegramUsername, Long id, LocalDate createdAt, LocalDate updatedAt,
                             Boolean isActive, Integer totalSpent, Integer bonusBalance) {
        super(firstName, lastName, birthday, chatId, phone, telegramUsername);
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isActive = isActive;
        this.totalSpent = totalSpent;
        this.bonusBalance = bonusBalance;
    }

    public ClientResponseDto(String firstName, String lastName,
                             LocalDate birthday, String chatId, String phone, String telegramUsername) {
        super(firstName, lastName, birthday, chatId, phone,
                telegramUsername);
    }

    public ClientResponseDto() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Integer getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(Integer totalSpent) {
        this.totalSpent = totalSpent;
    }

    public Integer getBonusBalance() {
        return bonusBalance;
    }

    public void setBonusBalance(Integer bonusBalance) {
        this.bonusBalance = bonusBalance;
    }
}
