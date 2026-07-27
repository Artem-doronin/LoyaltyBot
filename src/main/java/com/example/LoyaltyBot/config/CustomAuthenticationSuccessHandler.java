package com.example.LoyaltyBot.config;

import com.example.LoyaltyBot.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(g -> Objects.equals(g.getAuthority(), "ROLE_ADMIN"));

       User user = (User) authentication.getPrincipal();
        boolean userShouldChangePassword = user.getShouldChangePassword();

        if(userShouldChangePassword) {
            response.sendRedirect("/смена временного пароля ");
            //todo нужно запретить доступ к ресурсам если userShouldChangePassword true ,доступ разрешить только к форме смены пароля
        }

        if (isAdmin) {
            response.sendRedirect("/users");
        }else {
            response.sendRedirect("/clients");
        }
    }
}
