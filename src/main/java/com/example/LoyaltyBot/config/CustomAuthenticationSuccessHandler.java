package com.example.LoyaltyBot.config;

import com.example.LoyaltyBot.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j  // ← Добавляем логирование
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {


        log.info("🔐 Аутентификация успешна");
        log.debug("  Session ID: {}", request.getSession().getId());


        User user = (User) authentication.getPrincipal();
        log.info("👤 Пользователь: {}", user.getUsername());
        log.info("  shouldChangePassword: {}", user.getShouldChangePassword());


        if (user.getShouldChangePassword()) {
            log.warn("⚠️ Пользователь {} должен сменить пароль → редирект на /users/change_password",
                    user.getUsername());
            response.sendRedirect("/users/change_password");
            return;
        }


        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(g -> g.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            log.info("✅ Администратор {} → редирект на /users", user.getUsername());
            response.sendRedirect("/users");
        } else {
            log.info("✅ Пользователь {} → редирект на /clients", user.getUsername());
            response.sendRedirect("/clients");
        }

        log.debug("✅ AuthenticationSuccessHandler завершил работу");
    }
}