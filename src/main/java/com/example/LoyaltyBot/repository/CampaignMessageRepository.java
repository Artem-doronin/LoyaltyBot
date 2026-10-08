package com.example.LoyaltyBot.repository;

import com.example.LoyaltyBot.entity.campaigns.CampaignMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignMessageRepository extends JpaRepository<CampaignMessage,Long> {
    List<CampaignMessage> findByCampaignId(Long campaignId);
}
