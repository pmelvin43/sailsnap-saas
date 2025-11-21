package com.sailsnap.backend.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sailsnap.backend.entities.Business;
import com.sailsnap.backend.repositories.BusinessRepository;
import com.sailsnap.backend.repositories.S3Repository;

@Service
public class BusinessService {

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired // Make sure this is added
    private S3Repository s3Repository;

    public Business getProfile(long id) {
        return businessRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Business not found"));
    }

    public Business updateLogo(long id, String logoKey) {
        Business business = getProfile(id);
        business.setLogoKey(logoKey);
        business.setUpdatedAt(LocalDateTime.now());
        return businessRepository.save(business);
    }

    public Business updateColors(long id, String primary, String secondary) {
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

        Business savedBusiness = businessRepository.save(business);
        s3Repository.createBucket(business.getBusinessName());
        return savedBusiness;
    }

    public Business login(String email, String password) {
        Business business = businessRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // TODO: Use proper password hashing (BCrypt)
        if (!business.getPassword().equals(password)) {
            throw new RuntimeException("Invalid email or password");
        }

        business.setLastLoginAt(LocalDateTime.now());
        return businessRepository.save(business);
    }
}
