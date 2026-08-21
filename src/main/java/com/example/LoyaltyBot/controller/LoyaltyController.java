package com.example.LoyaltyBot.controller;

import com.example.LoyaltyBot.dto.bonus.ClientBonusTransactionDto;
import com.example.LoyaltyBot.entity.OperationType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/api/loyalty")
@AllArgsConstructor
public class LoyaltyController {

    @GetMapping
    public String showBonusPage(
            @ModelAttribute("operation") ClientBonusTransactionDto operation,
            Model model) {
        if (operation == null) {
            operation = ClientBonusTransactionDto.builder()
                    .operationType(OperationType.ACCRUAL)
                    .build();
        }
        model.addAttribute("operation", operation);
        model.addAttribute("pageTitle", "Управление бонусами");
        model.addAttribute("found", false);

        return "loyalty/form-loyalty";
    }

    @GetMapping("/reset")
    public String resetForm() {
        log.info("Сброс формы");
        return "redirect:/loyalty";
    }

}