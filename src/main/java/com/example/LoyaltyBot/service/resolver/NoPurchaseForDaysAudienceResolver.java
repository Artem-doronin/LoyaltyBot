package com.example.LoyaltyBot.service.resolver;

import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.entity.OperationType;
import com.example.LoyaltyBot.entity.campaigns.AudienceParams;
import com.example.LoyaltyBot.entity.campaigns.AudienceType;
import com.example.LoyaltyBot.repository.ClientRepository;
import com.example.LoyaltyBot.util.AudienceParamsValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NoPurchaseForDaysAudienceResolver implements AudienceResolver {

    private final ClientRepository clientRepository;
    private final AudienceParamsValidator audienceParamsValidator;

    @Override
    public AudienceType supports() {
        return AudienceType.NO_PURCHASE_FOR_DAYS;
    }

    @Override
    public List<Client> resolveBatch(AudienceParams params, Long afterId, int limit) {
        audienceParamsValidator.validate(AudienceType.NO_PURCHASE_FOR_DAYS,params);

        List<String> purchaseTypes = OperationType.PURCHASE_TYPES.stream()
                .map(Enum::name)
                .toList();
        LocalDateTime threshold = LocalDateTime.now().minusDays(params.days());

        return clientRepository.findActiveRegisteredNoPurchaseBatch(
                purchaseTypes, threshold, afterId, PageRequest.of(0, limit));
    }
}
