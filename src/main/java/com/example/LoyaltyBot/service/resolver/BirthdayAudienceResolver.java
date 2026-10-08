package com.example.LoyaltyBot.service.resolver;

import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.entity.campaigns.AudienceParams;
import com.example.LoyaltyBot.entity.campaigns.AudienceType;
import com.example.LoyaltyBot.repository.ClientRepository;
import com.example.LoyaltyBot.util.AudienceParamsValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BirthdayAudienceResolver implements AudienceResolver {

    private final ClientRepository clientRepository;
    private final AudienceParamsValidator validator;
    @Value("${campaign.timezone}")
    private String timedZone;


    @Override
    public AudienceType supports() {
        return AudienceType.BIRTHDAY;
    }
//todo 29 февраля — пока пропускаем в невисокосный год

    @Override
    public List<Client> resolveBatch(AudienceParams params, Long afterId, int limit) {
        validator.validate(AudienceType.BIRTHDAY, params);
        LocalDate today = LocalDate.now(ZoneId.of(timedZone));
        int month = today.getMonthValue();
        int days = today.getDayOfMonth();
        return clientRepository.findActiveRegisteredBirthdayBatch(
                month, days, afterId, PageRequest.of(0, limit));
    }
}
