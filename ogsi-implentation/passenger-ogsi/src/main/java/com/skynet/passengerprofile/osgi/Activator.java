package com.skynet.passengerprofile.osgi;

import com.skynet.passengerprofile.osgi.api.PassengerProfileService;
import com.skynet.passengerprofile.osgi.impl.PassengerProfileServiceImpl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * Bundle Activator for Passenger Profile OSGi Bundle
 * 
 * This class manages the lifecycle of the bundle:
 * - start() is called when the bundle is started
 * - stop() is called when the bundle is stopped
 * 
 * OSGi Key Concept: Service Registry
 * - Services are registered in start()
 * - Services are unregistered in stop()
 * - Other bundles can discover and use these services
 */
public class Activator implements BundleActivator {
    
    private ServiceRegistration<PassengerProfileService> serviceRegistration;
    
    /**
     * Called when the bundle is started
     * This is where we register our services in the OSGi Service Registry
     */
    @Override
    public void start(BundleContext context) throws Exception {
        System.out.println("========================================");
        System.out.println("🚀 Passenger Profile Bundle STARTING...");
        System.out.println("========================================");
        
        // Create service implementation
        PassengerProfileService service = new PassengerProfileServiceImpl();
        
        // Register service in OSGi Service Registry
        serviceRegistration = context.registerService(
            PassengerProfileService.class,
            service,
            null  // No service properties for now
        );
        
        System.out.println("✅ PassengerProfileService registered in OSGi Service Registry");
        System.out.println("   Service ID: " + serviceRegistration.getReference().getProperty("service.id"));
        System.out.println("   Bundle ID: " + context.getBundle().getBundleId());
        System.out.println("   Bundle Name: " + context.getBundle().getSymbolicName());
        System.out.println("   Bundle Version: " + context.getBundle().getVersion());
        System.out.println("");
        System.out.println("📋 Available Service Methods:");
        System.out.println("   - getPassenger(Long id)");
        System.out.println("   - updatePassenger(Long id, Passenger passenger)");
        System.out.println("   - getProfileCompleteness(Long id)");
        System.out.println("   - getMissingFields(Long id)");
        System.out.println("   - ... and more");
        System.out.println("");
        System.out.println("🎯 Other bundles can now discover and use this service!");
        System.out.println("========================================");
    }
    
    /**
     * Called when the bundle is stopped
     * This is where we clean up and unregister our services
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        System.out.println("========================================");
        System.out.println("🛑 Passenger Profile Bundle STOPPING...");
        System.out.println("========================================");
        
        // Unregister service (though OSGi does this automatically)
        if (serviceRegistration != null) {
            serviceRegistration.unregister();
            System.out.println("✅ PassengerProfileService unregistered");
        }
        
        System.out.println("👋 Passenger Profile Bundle stopped gracefully");
        System.out.println("========================================");
    }
}
