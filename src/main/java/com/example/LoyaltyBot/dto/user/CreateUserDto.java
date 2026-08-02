package com.example.LoyaltyBot.dto.user;

import com.example.LoyaltyBot.entity.Role;
import com.example.LoyaltyBot.entity.User;
import lombok.Builder;

@Builder
public record CreateUserDto(
        String username,
        String email,
        Long role_id,
        Boolean enabled
) {
    // todo правильно ли здесь заполнять поля ?
    //  т.к. это дто create то наверное имеет место быть

    public User toUser(Role role, String encodedPassword) {
        return User.builder()
                .username(username)
                .password(encodedPassword)
                .email(email)
                .enabled(enabled)
                .shouldChangePassword(true)
                .account_non_expired(true)
                .account_non_locked(true)
                .credentials_non_expired(true)
                .role(role)
                .build();
    }
}
