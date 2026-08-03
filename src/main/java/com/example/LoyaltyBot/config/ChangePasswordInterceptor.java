package com.example.LoyaltyBot.config;

import com.example.LoyaltyBot.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class ChangePasswordInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        log.debug("Interceptor: {}", requestURI);

        if (isAllowedUrl(requestURI)) {
            log.debug("Разрешенный URL, пропускаем");
            return true;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())) {
            log.debug("Не авторизован, пропускаем");
            return true;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User)) {
            log.debug("Не User, пропускаем");
            return true;
        }

        User user = (User) principal;

        if (user.getShouldChangePassword()) {
            log.warn("Блокировка доступа: {} пытался зайти на {}",
                    user.getUsername(), requestURI);
            response.sendRedirect("/users/change_password");
            return false;
        }

        log.debug("Пропускаем: {}", user.getUsername());
        return true;
    }

    private boolean isAllowedUrl(String requestURI) {
        return requestURI.startsWith("/auth/login") ||
                requestURI.startsWith("/perform-login") ||
                requestURI.startsWith("/auth/error") ||
                requestURI.startsWith("/logout") ||
                requestURI.startsWith("/users/change_password") ||
                requestURI.startsWith("/css") ||
                requestURI.startsWith("/js") ||
                requestURI.startsWith("/images");
    }
}