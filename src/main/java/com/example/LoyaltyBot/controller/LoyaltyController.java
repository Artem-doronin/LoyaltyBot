package com.example.LoyaltyBot.controller;

import com.example.LoyaltyBot.dto.bonus.BonusOperationDto;
import com.example.LoyaltyBot.dto.client.ClientResponseSearchDto;
import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.entity.OperationType;
import com.example.LoyaltyBot.service.BonusService;
import com.example.LoyaltyBot.service.ClientService;
import com.example.LoyaltyBot.service.LoyaltyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/loyalty")
@AllArgsConstructor
public class LoyaltyController {
    private final ClientService clientService;
    private final LoyaltyService loyaltyService;
    private final BonusService bonusService;

    @GetMapping
    public String showBonusPage(
            @ModelAttribute("operation") BonusOperationDto operation,
            Model model) {
        if (operation == null) {
            operation = BonusOperationDto.builder().build();
        }
        model.addAttribute("operation", operation);
        model.addAttribute("pageTitle", "Управление бонусами");
        model.addAttribute("found", false);

        return "loyalty/form-loyalty";
    }

    @GetMapping("/search")
    @ResponseBody
    public List<ClientResponseSearchDto> searchClients(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit) {
        log.debug("Поиск клиентов по запросу: {}", query);
        if (query == null || query.trim().isEmpty()) {
            return List.of();
        }
        List<ClientResponseSearchDto> results = clientService.searchClients(query);
        if (limit > 0 && results.size() > limit) {
            return results.subList(0, limit);
        }
        return results;
    }

    @PostMapping("/find")
    public String findClient(
            @ModelAttribute("operation") BonusOperationDto operation, Model model) {

        log.info("Поиск клиента по номеру: {}", operation.phoneNumber());
        String phoneNumber = operation.phoneNumber().trim();
        Client client = clientService.findByPhoneNumber(phoneNumber);
        BigDecimal bonusAmount = bonusService.getAmount(client.getId());
        model.addAttribute("client", client);
        model.addAttribute("bonusAmount", bonusAmount);
        model.addAttribute("success", "Клиент найден!");

        model.addAttribute("operation", operation);
        return "loyalty/form-loyalty";
    }

    @PostMapping("/operation")
    public String executeOperation(
            @ModelAttribute("operation") BonusOperationDto operation,
            Model model) {
        loyaltyService.processBonusOperation(operation);
        model.addAttribute("operation", operation);

        String operationMessage = operation.operationType() == OperationType.ACCRUAL
                ? " Бонусы успешно начислены!"
                : " Бонусы успешно списаны!";

        model.addAttribute("success", operationMessage);

        return "loyalty/form-loyalty";
    }

    @GetMapping("/reset")
    public String resetForm() {
        log.info("Сброс формы");
        return "redirect:/loyalty";
    }
}