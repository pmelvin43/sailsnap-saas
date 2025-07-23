package com.sailsnap.backend.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sailsnap.backend.entities.Payment;
import com.sailsnap.backend.repositories.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@Service
@Transactional
public class PaymentService {
    private final PaymentRepository paymentRepo;

    @Autowired
    public PaymentService(PaymentRepository paymentRepo, @Value("${stripe.secret.key}") String stripeSecretKey) {
        this.paymentRepo = paymentRepo;
        Stripe.apiKey = stripeSecretKey; // Initialize Stripe once
    }

    @Transactional
    public Payment createPaymentIntent(int businessId, double amount, String currency, Integer galleryId,
            String customerEmail) {
        try {
            long amountInCents = (long) (amount * 100);

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency(currency)
                    .setReceiptEmail(customerEmail)
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);

            Payment payment = new Payment();
            payment.setBusinessId(businessId);
            payment.setAmount(amount);
            payment.setCurrency(currency);
            payment.setGalleryId(galleryId);
            payment.setCustomerEmail(customerEmail);
            payment.setStripePaymentIntentId(intent.getId());
            payment.setCreatedAt(LocalDateTime.now());
            payment.setStatus("pending");

            paymentRepo.save(payment);

            // Instead of overwriting the ID, just return the client secret separately.
            // You can make a custom response object for the controller.
            return payment;

        } catch (Exception e) {
            throw new RuntimeException("Stripe payment intent creation failed", e);
        }
    }

    public void handleStripeWebhook(String eventType, String paymentIntentId) {
        paymentRepo.findByStripePaymentIntentId(paymentIntentId).ifPresent(payment -> {
            if ("payment_intent.succeeded".equals(eventType)) {
                payment.setStatus("succeeded");
            } else if ("payment_intent.payment_failed".equals(eventType)) {
                payment.setStatus("failed");
            }
            paymentRepo.save(payment);
        });
    }

    public List<Payment> getPaymentsForBusiness(int businessId) {
        return paymentRepo.findByBusinessId(businessId);
    }
}
