package com.example.LoyaltyBot.service.resolver;

import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.entity.campaigns.AudienceParams;
import com.example.LoyaltyBot.entity.campaigns.AudienceType;

import java.util.List;

public interface AudienceResolver {
    AudienceType supports();
    List<Client> resolveBatch(AudienceParams params, Long afterId, int limit);


}
