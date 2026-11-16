package com.example.LoyaltyBot.service.resolver;

import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.entity.campaigns.AudienceParams;
import com.example.LoyaltyBot.entity.campaigns.AudienceType;
import com.example.LoyaltyBot.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AllClientsAudienceResolver implements AudienceResolver {

    private final ClientRepository clientRepository;

    @Override
    public AudienceType supports() {
        return AudienceType.ALL_CLIENTS;
    }

    @Override
    public List<Client> resolveBatch(AudienceParams params, Long afterId, int limit) {
        return clientRepository.findActiveRegisteredBatch(afterId, PageRequest.of(0, limit));
    }
}
