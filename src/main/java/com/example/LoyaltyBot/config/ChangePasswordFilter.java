package com.example.LoyaltyBot.config;

import com.example.LoyaltyBot.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class ChangePasswordFilter extends OncePerRequestFilter {

    private static final List<String> ALLOWED_URLS = Arrays.asList(
            "/change-password",
            "/auth/login",
            "/perform-login",
            "/logout",
            "/css",
            "/js",
            "/images"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User)) {
            filterChain.doFilter(request, response);
            return;
        }
        User user = (User) principal;
        String requestURI = request.getRequestURI();

        if (user.getShouldChangePassword()) {

            boolean isAllowed = ALLOWED_URLS.stream()
                    .anyMatch(requestURI::startsWith);


            if (!isAllowed) {
                response.sendRedirect("/change-password");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}