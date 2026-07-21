package com.example.LoyaltyBot.dto.client;

import com.example.LoyaltyBot.entity.RegistrationState;
import java.time.LocalDate;

public class ClientResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private Long chatId;
    private Long telegramUserId;
    private String phone;
    private String telegramUsername;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private Boolean isActive;
    private Integer totalSpent;
    private Integer bonusBalance;
    private RegistrationState registrationState;

    public ClientResponseDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getTelegramUserId() {
        return telegramUserId;
    }

    public void setTelegramUserId(Long telegramUserId) {
        this.telegramUserId = telegramUserId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTelegramUsername() {
        return telegramUsername;
    }

    public void setTelegramUsername(String telegramUsername) {
        this.telegramUsername = telegramUsername;
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

    public RegistrationState getRegistrationState() {
        return registrationState;
    }

    public void setRegistrationState(RegistrationState registrationState) {
        this.registrationState = registrationState;
    }

    @Override
    public String toString() {
        return "ClientResponseDto{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthday=" + birthday +
                ", chatId='" + chatId + '\'' +
                ", telegramUserId='" + telegramUserId + '\'' +
                ", phone='" + phone + '\'' +
                ", telegramUsername='" + telegramUsername + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", isActive=" + isActive +
                ", totalSpent=" + totalSpent +
                ", bonusBalance=" + bonusBalance +
                ", registrationState=" + registrationState +
                '}';
    }
}