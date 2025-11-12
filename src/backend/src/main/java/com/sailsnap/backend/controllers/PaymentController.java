package com.sailsnap.backend.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sailsnap.backend.entities.Payment;
import com.sailsnap.backend.services.PaymentService;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // create a payment intent
    @PostMapping("/create-intent")
    public Payment createPayment(@RequestParam int businessId,
                                 @RequestParam double amount,
                                 @RequestParam String currency,
                                 @RequestParam Integer galleryId,
                                 @RequestParam String customerEmail) {
        // MVP: Simply create a Stripe payment intent and save it in our DB
        // TODO (non-MVP):
        // - Validate businessId and galleryId exist
        // - Check that the gallery has not already been purchased
        // - Apply taxes, discounts, or currency conversions if needed
        // - Return more detailed response info (like client_secret for frontend checkout)
        return paymentService.createPaymentIntent(businessId, amount, currency, galleryId, customerEmail);
    }

    // stripe webhook endpoint (MVP simplified)
    @PostMapping("/webhook")
    public String handleWebhook(@RequestBody Map<String, Object> payload) {
        // MVP: Accept simple JSON payload { "eventType": "...", "paymentIntentId": "..." }
        String eventType = (String) payload.get("eventType");
        String paymentIntentId = (String) payload.get("paymentIntentId");

        if (eventType == null || paymentIntentId == null) {
            return "Invalid payload";
        }

        // MVP: Update payment status and mark gallery as paid
        paymentService.handleStripeWebhook(eventType, paymentIntentId);

        // TODO (non-MVP):
        // - Verify Stripe signature header to ensure webhook authenticity
        // - Handle more event types (e.g., payment_intent.payment_failed, refund events)
        // - Trigger email notification to customer/business after successful payment
        // - Integrate analytics or logging for auditing payments
        // - Retry failed updates in case of temporary DB/network issues
        return "Webhook processed";
    }

    // list all payments for a business
    @GetMapping("/business/{businessId}")
    public List<Payment> getPayments(@PathVariable int businessId) {
        // MVP: Simply return all payments for testing/inspection
        // TODO (non-MVP):
        // - Implement pagination for large datasets
        // - Filter by status, date range, or gallery
        // - Only allow authorized business/admin users to view payments
        return paymentService.getPaymentsForBusiness(businessId);
    }
}
