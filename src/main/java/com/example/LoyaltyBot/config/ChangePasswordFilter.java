package com.example.LoyaltyBot.config;

import com.example.LoyaltyBot.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class ChangePasswordFilter extends OncePerRequestFilter {

    private static final List<String> ALLOWED_URLS = Arrays.asList(
            "/users/change_password",
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

        String requestURI = request.getRequestURI();


        log.debug("🔍 ChangePasswordFilter для: {}", requestURI);


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            log.debug("  → Не авторизован, пропускаем");
            filterChain.doFilter(request, response);
            return;
        }


        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User)) {
            log.debug("  → Не User, пропускаем");
            filterChain.doFilter(request, response);
            return;
        }

        User user = (User) principal;
        log.debug("  user = {}, shouldChangePassword = {}", user.getUsername(), user.getShouldChangePassword());

        if (user.getShouldChangePassword()) {
            boolean isAllowed = ALLOWED_URLS.stream()
                    .anyMatch(requestURI::startsWith);
            log.debug("  isAllowed = {}", isAllowed);

            // ❌ Если НЕ разрешен → редирект
            if (!isAllowed) {
                log.info("  → РЕДИРЕКТ на /users/change_password: {}", requestURI);
                response.sendRedirect("/users/change_password");
                return;
            }
        }

        log.debug("  → Пропускаем");
        filterChain.doFilter(request, response);
    }
}