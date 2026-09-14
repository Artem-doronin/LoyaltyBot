package com.example.LoyaltyBot.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/loyalty")
@AllArgsConstructor
public class LoyaltyController {

    @GetMapping
    public String showBonusPage() {
        return "loyalty/form-loyalty";
    }

    @GetMapping("/reset")
    public String resetForm() {
        log.info("Сброс формы");
        return "redirect:/loyalty";
    }
}