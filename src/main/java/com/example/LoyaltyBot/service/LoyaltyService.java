package com.example.LoyaltyBot.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoyaltyService {
    private final ClientService clientService;
    private final BonusService bonusService;
    private final OperationService operationService;
    private final UserService userService;
    private final NotificationService notificationService;


}
