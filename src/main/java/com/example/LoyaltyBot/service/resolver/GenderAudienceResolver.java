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
public class GenderAudienceResolver implements AudienceResolver {
    private final ClientRepository clientRepository;
    private final AudienceParamsValidator audienceParamsValidator;

    @Override
    public AudienceType supports() {
        return AudienceType.GENDER;
    }

    @Override
    public List<Client> resolveBatch(AudienceParams params, Long afterId, int limit) {
        audienceParamsValidator.validate(AudienceType.GENDER, params);

        return clientRepository.findActiveRegisteredGenderBatch(
                params.gender().name(), afterId, PageRequest.of(0, limit));
    }
}
