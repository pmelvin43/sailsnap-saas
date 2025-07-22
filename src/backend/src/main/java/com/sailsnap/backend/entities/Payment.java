package com.sailsnap.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "business_id", nullable = false)
    private int businessId; // which buisness this payment is for

    @Column(name = "gallery_id")
    private Integer galleryId; // if payment is for a specific gallery purchase

    @Column(name = "amount", nullable = false)
    private double amount; // total charge

    @Column(name = "currency", nullable = false)
    private String currency; // USD, CAD, etc.

    @Column(name = "stripe_payment_id", nullable = false, unique = true)
    private String stripePaymentIntentId; // to query stripe later

    @Column(name = "amount", nullable = false)
    private String status; // succeeded, pending, failed

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "customer_email")
    private String customerEmail; // for sending recipts later

}
