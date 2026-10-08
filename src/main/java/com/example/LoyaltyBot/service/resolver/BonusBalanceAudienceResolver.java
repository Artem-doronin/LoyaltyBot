package com.example.LoyaltyBot.service.resolver;

import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.entity.campaigns.AudienceParams;
import com.example.LoyaltyBot.entity.campaigns.AudienceType;
import com.example.LoyaltyBot.repository.ClientRepository;
import com.example.LoyaltyBot.util.AudienceParamsValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BonusBalanceAudienceResolver implements AudienceResolver {

    private final ClientRepository clientRepository;
    private final AudienceParamsValidator validateAudience;

    @Override
    public AudienceType supports() {
        return AudienceType.BONUS_BALANCE_GREATER_THAN;
    }

    @Override
    public List<Client> resolveBatch(AudienceParams params, Long afterId, int limit) {
        validateAudience.validate(AudienceType.BONUS_BALANCE_GREATER_THAN,params);
        return clientRepository.findActiveRegisteredWithBonusGreaterThan(
                params.bonusBalance(),
                afterId,
                PageRequest.of(0, limit));
    }
}
