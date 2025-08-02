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
        Stripe.apiKey = stripeSecretKey; // initialize stripe
    }

    @Transactional
    @SuppressWarnings("UseSpecificCatch") // this is where the customer attempts to "checkout" for a photo album.
    public Payment createPaymentIntent(int businessId, double amount, String currency, Integer galleryId,
            String customerEmail) {
        try {
            long amountInCents = (long) (amount * 100); // required to be in cents with a long for stripe

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder() // build the parameters needded for
                                                                                   // string
                    .setAmount(amountInCents)
                    .setCurrency(currency)
                    .setReceiptEmail(customerEmail)
                    .build();

            PaymentIntent intent = PaymentIntent.create(params); // call stripe API and create an intent object

            Payment payment = new Payment();
            payment.setBusinessId(businessId);
            payment.setAmount(amount);
            payment.setCurrency(currency);
            payment.setGalleryId(galleryId);
            payment.setCustomerEmail(customerEmail);
            payment.setStripePaymentIntentId(intent.getId());
            payment.setCreatedAt(LocalDateTime.now());
            payment.setStatus("pending"); // this will be handled by the webhook

            paymentRepo.save(payment); // create our payment object with all metadata needed and save it to our payment
                                       // repo
            return payment;

        } catch (Exception e) {
            throw new RuntimeException("Stripe payment intent creation failed", e);
        }
    }

    public void handleStripeWebhook(String eventType, String paymentIntentId) { // use query set in the repo to find the
                                                                                // payment
        paymentRepo.findByStripePaymentIntentId(paymentIntentId).ifPresent(payment -> {
            if ("payment_intent.succeeded".equals(eventType)) {
                payment.setStatus("succeeded");
            } else if ("payment_intent.payment_failed".equals(eventType)) {
                payment.setStatus("failed");
            }
            paymentRepo.save(payment);
        });
    }

    public List<Payment> getPaymentsForBusiness(int businessId) { // just a list for businesses to get payments by id
        return paymentRepo.findByBusinessId(businessId);
    }
}
