package com.example.LoyaltyBot.controller;

import com.example.LoyaltyBot.dto.bonus.BonusOperationDto;
import com.example.LoyaltyBot.dto.client.ClientResponseSearchDto;
import com.example.LoyaltyBot.service.ClientService;
import com.example.LoyaltyBot.service.LoyaltyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/loyalty")
@AllArgsConstructor
public class LoyaltyController {
    private final ClientService clientService;
    private final LoyaltyService loyaltyService;



    @GetMapping
    public String showBonusPage(
            @ModelAttribute("operation") BonusOperationDto operation, Model model) {
        if (operation == null) {
            operation = BonusOperationDto.builder().build();
        }
        model.addAttribute("operation", operation);
        model.addAttribute("pageTitle", "Управление бонусами");

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
}
