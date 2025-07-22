package com.sailsnap.backend.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sailsnap.backend.entities.Business;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Integer> {

    // find a business by its exact name
    Business findByBusinessName(String businessName);

    // find a business by email (useful for login)
    Business findByEmail(String email);

    // search for businesses by partial name (case-sensitive by default)
    List<Business> findByBusinessNameContaining(String keyword);

    // list all businesses ordered by most recently created
    List<Business> findAllByOrderByCreatedAtDesc();

    // list all active businesses
    List<Business> findByIsActiveTrue();

    // find businesses that registered within a certain date range
    List<Business> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // find businesses by subscription status (such as "active",
    // "canceled")
    List<Business> findBySubscriptionStatus(String status);

    // find businesses by stripe customer ID
    Business findByStripeCustomerId(String stripeCustomerId);

}
