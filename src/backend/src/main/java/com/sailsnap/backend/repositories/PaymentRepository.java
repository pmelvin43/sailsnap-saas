package com.sailsnap.backend.repositories;

import com.sailsnap.backend.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    // find all payments for a specific business
    List<Payment> findByBusinessId(int businessId);

    // find all payments in decending order
    List<Payment> findByBusinessIdOrderByCreatedAtDesc(int businessId);

    // find payments by status (for failed retries or pending processing)
    List<Payment> findByStatus(String status);

    // find all payments for a specific gallery
    List<Payment> findByGalleryId(int galleryId);

    // find all payments by customer email (for receipts or audits)
    List<Payment> findByCustomerEmail(String email);

    // find all payments in a date range
    List<Payment> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // find all payments by a stripe payment ID
    Payment findByStripePaymentIntentId(String paymentIntentId);

}
