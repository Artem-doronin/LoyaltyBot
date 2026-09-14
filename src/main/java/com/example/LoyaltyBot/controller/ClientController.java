package com.example.LoyaltyBot.controller;

import com.example.LoyaltyBot.service.ClientBonusBalancesService;
import com.example.LoyaltyBot.service.ClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/clients")
@Controller
public class ClientController {
    private final ClientService clientService;
    private final ClientBonusBalancesService bonusService;

    public ClientController(ClientService clientService, ClientBonusBalancesService bonusService) {
        this.clientService = clientService;
        this.bonusService = bonusService;
    }

    @GetMapping
    public String getAllClients(Model model) {
        model.addAttribute("clients", clientService.findAll());
        return "clients/list";
    }

    @GetMapping("/{id}")
    public String getClient(@PathVariable Long id, Model model) {
        model.addAttribute("client", clientService.findById(id));
        model.addAttribute("bonus", bonusService.getBonusDto(id));
        return "clients/client-detail";
    }

    @GetMapping("/delete/{id}")
    public String deleteClient(@PathVariable Long id) {
        clientService.deleteById(id);
        return "redirect:/clients";
    }
}
