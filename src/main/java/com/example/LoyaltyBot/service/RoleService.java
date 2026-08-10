package com.example.LoyaltyBot.service;

import com.example.LoyaltyBot.entity.Role;
import com.example.LoyaltyBot.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findById(Long id) {
        return roleRepository.findById(id).orElseThrow(()->new EntityNotFoundException("not found role"));
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
