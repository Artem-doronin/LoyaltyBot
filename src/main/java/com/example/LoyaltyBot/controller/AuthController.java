package com.example.LoyaltyBot.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @GetMapping("/login")
    public String loginPage() {
        return "/auth/login";
    }

    @GetMapping("/error")
    public String loginError(Model model) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));

        log.error("❌ Ошибка входа - неверное имя пользователя или пароль");

        model.addAttribute("status", "401");
        model.addAttribute("error", "Неверное имя пользователя или пароль");
        model.addAttribute("userMessage", "Неверное имя пользователя или пароль");
        model.addAttribute("timestamp", timestamp);
        model.addAttribute("path", "/perform-login");

        return "user/error";
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            request.getSession().invalidate();
        }
        return "redirect:/auth/login";
    }
}
