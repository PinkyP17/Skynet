package com.skynet.payment.osgi;

import com.skynet.payment.osgi.api.PaymentService;
import com.skynet.payment.osgi.impl.PaymentServiceImpl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * Bundle Activator for Payment OSGi Bundle
 */
public class Activator implements BundleActivator {

    private ServiceRegistration<PaymentService> serviceRegistration;

    @Override
    public void start(BundleContext context) throws Exception {
        System.out.println("========================================");
        System.out.println("💳 Payment & Invoicing Bundle STARTING...");
        
        // Create service implementation
        PaymentService service = new PaymentServiceImpl();
        
        // Register service
        serviceRegistration = context.registerService(
            PaymentService.class,
            service,
            null
        );
        
        System.out.println("✅ PaymentService registered in OSGi Service Registry");
        System.out.println("========================================");
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        System.out.println("========================================");
        System.out.println("🛑 Payment & Invoicing Bundle STOPPING...");
        
        if (serviceRegistration != null) {
            serviceRegistration.unregister();
        }
        
        System.out.println("👋 Payment Bundle stopped");
        System.out.println("========================================");
    }
}
