package com.skynet.payment.osgi.client;

import com.skynet.payment.osgi.api.PaymentService;
import com.skynet.payment.osgi.model.Invoice;
import com.skynet.payment.osgi.model.Payment;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * Enhanced OSGi Client Bundle to test the Payment Service.
 * Demonstrates the OSGi Service Registry pattern with comprehensive logging.
 */
public class PaymentServiceClient implements BundleActivator {

    private ServiceReference<PaymentService> serviceReference;

    @Override
    public void start(BundleContext context) throws Exception {
        System.out.println("\n========================================");
        System.out.println("🔍 CLIENT: Payment Test Client STARTING...");
        System.out.println("========================================\n");

        // 1. Dynamic Service Lookup
        System.out.println("1️⃣  Looking up PaymentService in OSGi Registry...");
        serviceReference = context.getServiceReference(PaymentService.class);

        if (serviceReference != null) {
            // 2. Get the Service Implementation
            PaymentService paymentService = context.getService(serviceReference);
            
            System.out.println("✅ Service found!");
            System.out.println("   Service ID: " + serviceReference.getProperty("service.id"));
            System.out.println("   Bundle providing service: " + serviceReference.getBundle().getSymbolicName());
            System.out.println("");

            if (paymentService != null) {
                System.out.println("🚀 Running Payment Service Tests...\n");
                runTests(paymentService);
            } else {
                System.err.println("❌ Error: Service reference found, but service object is null.");
            }
        } else {
            System.err.println("❌ Error: PaymentService not found in Registry. Is the Provider bundle active?");
        }
        System.out.println("========================================");
    }

    private void runTests(PaymentService service) {
        // --- TEST 1: Process New Payment ---
        System.out.println("--- Test 1: Processing Payment ---");
        Payment newPayment = new Payment(150.00, "USD", "CREDIT_CARD", 101L);
        System.out.println("📤 Sending Payment: " + newPayment.getAmount() + " " + newPayment.getCurrency());
        
        Payment processed = service.processPayment(newPayment);
        
        if ("COMPLETED".equals(processed.getStatus())) {
            System.out.println("✅ Payment Processed Successfully!");
            System.out.println("   ID: " + processed.getId());
            System.out.println("   Status: " + processed.getStatus());
            System.out.println("   Timestamp: " + processed.getTransactionDate());
        } else {
            System.out.println("❌ Payment Failed.");
        }

        // --- TEST 2: Apply Promo Code ---
        System.out.println("\n--- Test 2: Applying Promo Code ---");
        String promoCode = "SKYNET2024";
        double original = 200.00;
        System.out.println("💳 Applying code '" + promoCode + "' to amount: $" + original);
        
        double discounted = service.applyPromoCode(promoCode, original);
        
        if (discounted < original) {
            System.out.println("✅ Promo Code Applied!");
            System.out.println("   Original: $" + original);
            System.out.println("   Discounted: $" + discounted);
            System.out.println("   Savings: $" + (original - discounted));
        } else {
            System.out.println("❌ Promo Code Invalid or Not Applied.");
        }

        // --- TEST 3: Generate Invoice ---
        System.out.println("\n--- Test 3: Generating Invoice ---");
        System.out.println("📄 Generating invoice for Payment ID: " + processed.getId());
        
        Invoice invoice = service.generateInvoice(processed.getId());
        
        if (invoice != null) {
            System.out.println("✅ Invoice Generated Successfully!");
            System.out.println("   Invoice #: " + invoice.getInvoiceNumber());
            System.out.println("   Amount: $" + invoice.getTotalAmount());
            System.out.println("   Date: " + invoice.getDateIssued());
        } else {
            System.out.println("❌ Invoice Generation Failed.");
        }
        
        // --- TEST 4: Refund Process ---
        System.out.println("\n--- Test 4: Processing Refund ---");
        System.out.println("💸 Requesting refund for Payment ID: " + processed.getId());
        
        boolean refundStatus = service.processRefund(processed.getId());
        
        if (refundStatus) {
            System.out.println("✅ Refund Processed Successfully!");
        } else {
            System.out.println("❌ Refund Failed.");
        }
        
        System.out.println("\n✅ All Payment Service tests completed!");
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        System.out.println("\n========================================");
        System.out.println("👋 CLIENT: Payment Test Client STOPPING...");
        
        // 3. Clean Up
        // It is best practice to unget the service when the bundle stops
        if (serviceReference != null) {
            context.ungetService(serviceReference);
            System.out.println("🔓 Service reference released");
        }
        System.out.println("========================================");
    }
}