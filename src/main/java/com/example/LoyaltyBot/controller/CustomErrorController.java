package com.example.LoyaltyBot.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@Slf4j
public class CustomErrorController implements org.springframework.boot.webmvc.error.ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {


        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object error = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object path = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);


        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));


        String userMessage = "Произошла ошибка";
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode == 404) userMessage = "Страница не найдена";
            else if (statusCode == 403) userMessage = "Доступ запрещен";
            else if (statusCode == 401) userMessage = "Требуется авторизация";
            else if (statusCode == 500) userMessage = "Внутренняя ошибка сервера";
        }


        log.error("❌ Ошибка: status={}, error={}, path={}, userMessage={}",
                status, error, path, userMessage);


        model.addAttribute("status", status);
        model.addAttribute("error", error != null ? error : "Неизвестная ошибка");
        model.addAttribute("path", path != null ? path : "неизвестный путь");
        model.addAttribute("userMessage", userMessage);
        model.addAttribute("timestamp", timestamp);  // ← ДОБАВЛЯЕМ!

        return "user/error";
    }
}