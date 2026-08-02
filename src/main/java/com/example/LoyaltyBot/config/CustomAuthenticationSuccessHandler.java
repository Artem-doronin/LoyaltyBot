package com.example.LoyaltyBot.config;

import com.example.LoyaltyBot.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        System.out.println("=========================================");
        System.out.println("🚀🚀🚀 CustomAuthenticationSuccessHandler ВЫЗВАН! 🚀🚀🚀");
        System.out.println("  Session ID: " + request.getSession().getId());
        System.out.println("=========================================");

        // ✅ ПОЛУЧАЕМ ПОЛЬЗОВАТЕЛЯ
        User user = (User) authentication.getPrincipal();
        System.out.println("  User: " + user.getUsername());
        System.out.println("  shouldChangePassword: " + user.getShouldChangePassword());

        // ✅ ПРОВЕРКА: должен ли сменить пароль (ПРИОРИТЕТ 1)
        if (user.getShouldChangePassword()) {
            System.out.println("  → Перенаправление на /users/change_password");
            response.sendRedirect("/users/change_password");
            return;  // ← ВАЖНО! Прерываем выполнение
        }

        // ✅ ПРОВЕРКА: администратор или пользователь (ПРИОРИТЕТ 2)
        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(g -> g.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            System.out.println("  → Перенаправление на /users");
            response.sendRedirect("/users");
        } else {
            System.out.println("  → Перенаправление на /clients");
            response.sendRedirect("/clients");
        }
    }
}