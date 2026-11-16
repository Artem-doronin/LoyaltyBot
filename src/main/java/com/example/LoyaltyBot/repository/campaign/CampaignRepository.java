package com.example.LoyaltyBot.repository.campaign;

import com.example.LoyaltyBot.entity.campaigns.Campaign;
import com.example.LoyaltyBot.entity.campaigns.CampaignStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign,Long> {

    List<Campaign> findByIdAndStatus(Long campaignId, CampaignStatus status);
}
