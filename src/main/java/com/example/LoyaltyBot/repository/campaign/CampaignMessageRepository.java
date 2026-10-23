package com.example.LoyaltyBot.repository.campaign;

import com.example.LoyaltyBot.entity.campaigns.CampaignMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CampaignMessageRepository extends JpaRepository<CampaignMessage,Long> {
   Optional<CampaignMessage> findByCampaignId(Long campaignId);
}
