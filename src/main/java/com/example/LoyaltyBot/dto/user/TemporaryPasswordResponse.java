package com.example.LoyaltyBot.dto.user;

import lombok.Builder;

@Builder
public record TemporaryPasswordResponse(
        String login,
        String temporaryPassword,
        String action
) {

    //todo ты наверное скажешь дублирование кода ?
    // можно в метод передать стрингу

    public static TemporaryPasswordResponse forCreate(String username, String password) {
        return TemporaryPasswordResponse.builder()
                .login(username)
                .temporaryPassword(password)
                .action("CREATE")
                .build();
    }


    public static TemporaryPasswordResponse forReset(String username, String password) {
        return TemporaryPasswordResponse.builder()
                .login(username)
                .temporaryPassword(password)
                .action("RESET")
                .build();
    }
}
