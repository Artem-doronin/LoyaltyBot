package com.example.LoyaltyBot.config;

import com.example.LoyaltyBot.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;


public class ForcePasswordChangeFilter extends OncePerRequestFilter {

    private static final Set<String> ALLOWED_PATHS = Set.of(
            "/users/change_password",  // ← ДОБАВЛЕНО!
            "/auth/login",
            "/perform-login",
            "/logout",
            "/auth/error",
            "/css",
            "/js",
            "/images",
            "/favicon.ico",
            "/webjars"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()
                && auth.getPrincipal() instanceof User user
                && user.getShouldChangePassword()
                && ALLOWED_PATHS.stream().noneMatch(p -> request.getRequestURI().startsWith(p))) {

            response.sendRedirect("/users/change_password");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
