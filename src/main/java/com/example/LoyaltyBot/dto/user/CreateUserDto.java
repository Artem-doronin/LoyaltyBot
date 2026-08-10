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

    public User toUser(Role role, String encodedPassword) {
        return User.builder()
                .username(username)
                .password(encodedPassword)
                .email(email)
                .enabled(enabled)
                .shouldChangePassword(true)
                .role(role)
                .build();
    }
}
