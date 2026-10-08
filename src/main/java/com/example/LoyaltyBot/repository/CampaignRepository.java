package com.example.LoyaltyBot.repository;

import com.example.LoyaltyBot.entity.campaigns.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign,Long> {

    Optional<Campaign> findById(Long id);
}
