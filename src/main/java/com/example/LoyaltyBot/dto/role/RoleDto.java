package com.example.LoyaltyBot.dto.role;

import java.util.List;

public record RoleDto(
        Long id,
        String name,
        String description,
        List<Long> users_id
) {
}
