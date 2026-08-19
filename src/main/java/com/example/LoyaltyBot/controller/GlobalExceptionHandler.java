package com.example.LoyaltyBot.controller;

import com.example.LoyaltyBot.exception.InsufficientBonusException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientBonusException.class)
    public String handleInsufficientBonus(InsufficientBonusException e,
                                          Model model,
                                          RedirectAttributes redirectAttributes) {
        log.error("Недостаточно бонусов: {}", e.getMessage());
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/loyalty";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFound(EntityNotFoundException e,
                                       Model model,
                                       RedirectAttributes redirectAttributes) {
        log.error("Сущность не найдена: {}", e.getMessage());
        redirectAttributes.addFlashAttribute("error", "Запись не найдена: " + e.getMessage());
        return "redirect:/loyalty";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException e,
                                        Model model,
                                        RedirectAttributes redirectAttributes) {
        log.error("Неверный аргумент: {}", e.getMessage());
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/loyalty";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        log.error("Непредвиденная ошибка: {}", e.getMessage(), e);
        redirectAttributes.addFlashAttribute("error",
                "Произошла непредвиденная ошибка. Пожалуйста, попробуйте позже.");
        return "redirect:/loyalty";
    }
}