package com.skynet.payment.osgi.api;

import com.skynet.payment.osgi.model.Invoice;
import com.skynet.payment.osgi.model.Payment;
import java.util.List;

/**
 * Service Interface for Payment and Invoicing Module
 */
public interface PaymentService {
    
    /**
     * Process a new payment
     */
    Payment processPayment(Payment payment);
    
    /**
     * Generate invoice for a completed payment
     */
    Invoice generateInvoice(Long paymentId);
    
    /**
     * Process a refund for a transaction
     */
    boolean processRefund(Long paymentId);
    
    /**
     * Apply a promo code to get discount amount
     */
    double applyPromoCode(String promoCode, double originalAmount);
    
    /**
     * Get payment details by ID
     */
    Payment getPayment(Long id);
    
    /**
     * Get all payments
     */
    List<Payment> getAllPayments();
}
