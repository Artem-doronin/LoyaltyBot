package com.example.LoyaltyBot.repository;

import com.example.LoyaltyBot.entity.campaigns.CampaignAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignAttachmentRepository extends JpaRepository<CampaignAttachment,Long> {
    List<CampaignAttachment> findByCampaignMessageIdOrderBySortOrderAsc(Long campaignMessageId);
}
