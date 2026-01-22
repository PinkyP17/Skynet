package com.skynet.payment.osgi.impl;

import com.skynet.payment.osgi.api.PaymentService;
import com.skynet.payment.osgi.dao.PaymentDAO;
import com.skynet.payment.osgi.model.Invoice;
import com.skynet.payment.osgi.model.Payment;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of PaymentService
 */
public class PaymentServiceImpl implements PaymentService {

    private final PaymentDAO paymentDAO;

    public PaymentServiceImpl() {
        this.paymentDAO = new PaymentDAO();
    }

    @Override
    public Payment processPayment(Payment payment) {
        // Business Logic: Validate payment, check anti-fraud, etc.
        if (payment.getAmount() <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }
        
        // Simulate processing...
        payment.setStatus("COMPLETED");
        
        return paymentDAO.save(payment);
    }

    @Override
    public Invoice generateInvoice(Long paymentId) {
        Payment payment = paymentDAO.findById(paymentId);
        if (payment == null || !"COMPLETED".equals(payment.getStatus())) {
            return null;
        }

        Invoice invoice = new Invoice();
        invoice.setPaymentId(paymentId);
        invoice.setTotalAmount(payment.getAmount());
        invoice.setInvoiceNumber("INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        invoice.setDateIssued(payment.getTransactionDate());
        invoice.setCustomerName("Guest Passenger"); // Simplified
        
        return invoice;
    }

    @Override
    public boolean processRefund(Long paymentId) {
        Payment payment = paymentDAO.findById(paymentId);
        if (payment == null) return false;
        
        // Only completed payments can be refunded
        if ("COMPLETED".equals(payment.getStatus())) {
            return paymentDAO.updateStatus(paymentId, "REFUNDED");
        }
        return false;
    }

    @Override
    public double applyPromoCode(String promoCode, double originalAmount) {
        // Hardcoded promo codes for demonstration
        if ("SKYNET2024".equalsIgnoreCase(promoCode)) {
            return originalAmount * 0.90; // 10% off
        } else if ("WELCOME50".equalsIgnoreCase(promoCode)) {
            return Math.max(0, originalAmount - 50.0); // $50 off
        }
        return originalAmount;
    }

    @Override
    public Payment getPayment(Long id) {
        return paymentDAO.findById(id);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentDAO.findAll();
    }
}
