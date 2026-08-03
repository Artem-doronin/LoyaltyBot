package com.example.LoyaltyBot.dto.user;

import com.example.LoyaltyBot.entity.User;
import lombok.Builder;

@Builder
public record UserDto(
        Long id,
        String username,
        String password,
        String email,
        String roleName,
        Boolean enabled
) {

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .roleName(user.getRole().getName())
                .enabled(user.getEnabled())
                .build();
    }

    public User toUser() {
        return User.builder()
                .id(id)
                .username(username)
                .password(password)
                .email(email)
                .enabled(enabled)
                .build();
    }
}
