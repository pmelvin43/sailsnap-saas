package com.sailsnap.backend.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sailsnap.backend.entities.Business;
import com.sailsnap.backend.repositories.BusinessRepository;

@Service
public class BusinessService {

    @Autowired
    private BusinessRepository businessRepository;

    public Business getProfile(int id) {
        return businessRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Business not found"));
    }

    public Business updateLogo(int id, String logoKey) {
        Business business = getProfile(id);
        business.setLogoKey(logoKey);
        business.setUpdatedAt(LocalDateTime.now());
        return businessRepository.save(business);
    }

    public Business updateColors(int id, String primary, String secondary) {
        Business business = getProfile(id);
        business.setPrimaryColor(primary);
        business.setSecondaryColor(secondary);
        business.setUpdatedAt(LocalDateTime.now());
        return businessRepository.save(business);
    }

    public Business register(Business business) {
        business.setCreatedAt(LocalDateTime.now());
        business.setUpdatedAt(LocalDateTime.now());
        business.setActive(true);
        // TODO: hash password
        return businessRepository.save(business);
    }

    public Business login(String email, String password) {
        Business business = businessRepository.findByEmail(email);
        if (business == null || !business.getPassword().equals(password)) {
            // TODO: hash and compare passwords properly
            throw new RuntimeException("Invalid email or password");
        }
        business.setLastLoginAt(LocalDateTime.now());
        return businessRepository.save(business);
    }
}
