package com.example.LoyaltyBot.service;

import com.example.LoyaltyBot.service.resolver.AudienceResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CampaignService {
    private final AudienceResolver audienceResolver;
}
