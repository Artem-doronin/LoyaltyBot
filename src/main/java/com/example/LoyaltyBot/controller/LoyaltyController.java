package com.example.LoyaltyBot.controller;

import com.example.LoyaltyBot.dto.bonus.BonusOperationDto;
import com.example.LoyaltyBot.dto.client.ClientResponseSearchDto;
import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.service.BonusService;
import com.example.LoyaltyBot.service.ClientService;
import com.example.LoyaltyBot.service.LoyaltyService;
import jakarta.persistence.EntityNotFoundException;
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



    /**
     * GET /loyalty - Главная страница управления бонусами
     */
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

    /**
     * GET /loyalty/search - AJAX поиск клиентов для автокомплита
     */
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

    /**
     * ✅ POST /loyalty/find - Поиск клиента по номеру телефона
     */
    @PostMapping("/find")
    public String findClient(
            @ModelAttribute("operation") BonusOperationDto operation,
            Model model) {

        log.info("Поиск клиента по номеру: {}", operation.phoneNumber());

        try {
            if (operation.phoneNumber() == null || operation.phoneNumber().trim().isEmpty()) {
                model.addAttribute("error", "Введите номер телефона");
                model.addAttribute("found", false);
                model.addAttribute("operation", operation);
                return "loyalty/form-loyalty";
            }

            String phoneNumber = operation.phoneNumber().trim();

            Client client = clientService.findByPhoneNumber(phoneNumber);

            BigDecimal bonusAmount = bonusService.getAmount(client.getId());

            model.addAttribute("client", client);
            model.addAttribute("bonusAmount", bonusAmount);
            model.addAttribute("found", true);
            model.addAttribute("success", "Клиент найден!");

            log.info("Клиент найден: ID={}, Имя={}, Бонусы={}",
                    client.getId(), client.getFirstName(), bonusAmount);

        } catch (EntityNotFoundException e) {
            log.warn("Клиент не найден: {}", operation.phoneNumber());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("found", false);
        } catch (Exception e) {
            log.error("Ошибка при поиске клиента: {}", e.getMessage(), e);
            model.addAttribute("error", "Произошла ошибка при поиске клиента");
            model.addAttribute("found", false);
        }

        model.addAttribute("operation", operation);
        return "loyalty/form-loyalty";
    }
}
