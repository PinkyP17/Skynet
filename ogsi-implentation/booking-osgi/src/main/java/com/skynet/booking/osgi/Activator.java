package com.skynet.booking.osgi;

import com.skynet.booking.osgi.api.BookingService;
import com.skynet.booking.osgi.impl.BookingServiceImpl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {

    private ServiceRegistration<?> registration;

    @Override
    public void start(BundleContext context) throws Exception {
        System.out.println("========================================");
        System.out.println("🚀 Booking OSGi Bundle STARTING...");
        System.out.println("========================================");

        BookingService service = new BookingServiceImpl();
        registration = context.registerService(BookingService.class.getName(), service, null);

        System.out.println("✅ BookingService registered in OSGi Service Registry");
        System.out.println("🎯 Ready to handle flight reservations");
        System.out.println("========================================");
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        System.out.println("========================================");
        System.out.println("🛑 Booking OSGi Bundle STOPPING...");
        System.out.println("========================================");

        if (registration != null) {
            registration.unregister();
            System.out.println("✅ BookingService unregistered");
        }

        System.out.println("👋 Booking OSGi Bundle stopped gracefully");
        System.out.println("========================================");
    }
}
