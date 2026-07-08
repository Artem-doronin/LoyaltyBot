package com.example.LoyaltyBot.controller;

import com.example.LoyaltyBot.dto.ClientDto;
import com.example.LoyaltyBot.dto.TransactionDto;
import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.entity.Transaction;
import com.example.LoyaltyBot.service.ClientService;
import com.example.LoyaltyBot.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/clients")
@Controller
public class ClientController {
    private final ClientService clientService;
    private final TransactionService transactionService;

    public ClientController(ClientService clientService, TransactionService transactionService) {
        this.clientService = clientService;
        this.transactionService = transactionService;
    }

    @GetMapping
    public String getAllClients(Model model) {
         model.addAttribute("clients", clientService.findAll());
         return "clients/list";
    }

    @GetMapping("/{id}")
    public String getClient(@PathVariable Long id, Model model) {

        ClientDto clientDto = clientService.findById(id);
        if (clientDto == null) {
            return "redirect:/clients";
        }
        List<TransactionDto> transactionDtos = transactionService.findByClientId(id);

        model.addAttribute("client", clientDto);
        model.addAttribute("transactions", transactionDtos);

        return "clients/client-detail";
    }
}
