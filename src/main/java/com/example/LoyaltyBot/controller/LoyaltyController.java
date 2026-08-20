package com.example.LoyaltyBot.controller;

import com.example.LoyaltyBot.dto.bonus.BonusOperationDto;
import com.example.LoyaltyBot.dto.bonus.BonusResponseDto;
import com.example.LoyaltyBot.dto.client.ClientResponseDto;
import com.example.LoyaltyBot.dto.client.ClientResponseSearchDto;
import com.example.LoyaltyBot.entity.OperationType;
import com.example.LoyaltyBot.service.BonusService;
import com.example.LoyaltyBot.service.ClientService;
import com.example.LoyaltyBot.service.LoyaltyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
            operation = BonusOperationDto.builder()
                    .operationType(OperationType.ACCRUAL)
                    .build();
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
        log.debug("AJAX поиск клиентов по запросу: {}", query);
        if (query == null || query.trim().isEmpty()) {
            return List.of();
        }
        List<ClientResponseSearchDto> results = clientService.searchClients(query);
        if (limit > 0 && results.size() > limit) {
            return results.subList(0, limit);
        }
        return results;
    }

    @GetMapping("/find")
    @ResponseBody
    public ResponseEntity<?> findClientAjax(@RequestParam String phoneNumber) {
        log.info("AJAX поиск клиента по номеру: {}", phoneNumber);

        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(errorResponse("Введите номер телефона"));
        }

        String normalizedPhone = phoneNumber.trim();
        ClientResponseDto dto = clientService.findByPhoneNumber(normalizedPhone);
        BonusResponseDto bonusResponseDto = bonusService.getBonusDto(dto.id());

        Map<String, Object> data = new HashMap<>();
        data.put("id", dto.id());
        data.put("firstName", dto.firstName());
        data.put("phone", dto.phone());
        data.put("bonusAmount", bonusResponseDto.amount());
        data.put("rate", bonusResponseDto.rate());

        log.info("Клиент найден: ID={}, Имя={}, Бонусы={}",
                dto.id(), dto.firstName(), bonusResponseDto.amount());

        return ResponseEntity.ok(successResponse("Клиент найден!", data));
    }

    @PostMapping("/operation")
    @ResponseBody
    public ResponseEntity<?> executeOperationAjax(@RequestBody BonusOperationDto operation) {
        log.info("📝 AJAX операция: {} для клиента {}",
                operation.operationType(), operation.clientId());

        if (operation.clientId() == null) {
            return ResponseEntity.badRequest()
                    .body(errorResponse("ID клиента обязателен"));
        }

        if (operation.bonusAmount() == null || operation.bonusAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest()
                    .body(errorResponse("Сумма должна быть больше нуля"));
        }

        loyaltyService.processBonusOperation(operation);

        BigDecimal newBalance = bonusService.getAmount(operation.clientId());

        String operationMessage = operation.operationType() == OperationType.ACCRUAL
                ? String.format("Начислено %s бонусов!", operation.bonusAmount())
                : String.format("Списано %s бонусов!", operation.bonusAmount());

        Map<String, Object> data = new HashMap<>();
        data.put("clientId", operation.clientId());
        data.put("newBalance", newBalance);
        data.put("amount", operation.bonusAmount());
        data.put("operationAmount", operation.operationAmount());
        data.put("message", operationMessage);

        log.info("AJAX операция выполнена. Новый баланс: {}", newBalance);

        return ResponseEntity.ok(successResponse(operationMessage, data));
    }

    @GetMapping("/reset")
    public String resetForm() {
        log.info("Сброс формы");
        return "redirect:/loyalty";
    }

    private Map<String, Object> successResponse(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        response.put("data", data);
        return response;
    }

    private Map<String, Object> errorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", message);
        return response;
    }
}