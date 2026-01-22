package com.skynet.seatmanagement.osgi;

import com.skynet.seatmanagement.osgi.api.SeatManagementService;
import com.skynet.seatmanagement.osgi.client.SeatManagementClient;
import com.skynet.seatmanagement.osgi.impl.SeatManagementServiceImpl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * Bundle Activator for Seat Management OSGi Bundle
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
    
    private ServiceRegistration<SeatManagementService> serviceRegistration;
    private BundleContext bundleContext;
    
    /**
     * Called when the bundle is started
     * This is where we register our services in the OSGi Service Registry
     */
    @Override
    public void start(BundleContext context) throws Exception {
        try {
            this.bundleContext = context;
            
            System.out.println("========================================");
            System.out.println("🚀 Seat Management Bundle STARTING...");
            System.out.println("========================================");
            
            // Create service implementation
            System.out.println("[DEBUG] Creating SeatManagementServiceImpl...");
            SeatManagementService service = new SeatManagementServiceImpl();
            System.out.println("[DEBUG] Service implementation created successfully");
            
            // Register service in OSGi Service Registry
            System.out.println("[DEBUG] Registering service in OSGi Service Registry...");
            serviceRegistration = context.registerService(
                SeatManagementService.class,
                service,
                null  // No service properties for now
            );
            
            System.out.println("✅ SeatManagementService registered in OSGi Service Registry");
            System.out.println("   Service ID: " + serviceRegistration.getReference().getProperty("service.id"));
            System.out.println("   Bundle ID: " + context.getBundle().getBundleId());
            System.out.println("   Bundle Name: " + context.getBundle().getSymbolicName());
            System.out.println("   Bundle Version: " + context.getBundle().getVersion());
            System.out.println("");
            System.out.println("📋 Available Service Methods:");
            System.out.println("   - getAllSeats()");
            System.out.println("   - getSeatById(Integer id)");
            System.out.println("   - getSeatsByType(String type)");
            System.out.println("   - searchSeatByLabel(String label)");
            System.out.println("   - isSeatAvailable(Integer seatId, Integer flightId)");
            System.out.println("   - getAvailableSeats(Integer flightId)");
            System.out.println("   - getSeatAvailabilityStats(Integer flightId)");
            System.out.println("   - filterSeatsByType(String type)");
            System.out.println("   - searchSeats(String pattern)");
            System.out.println("   - ... and more");
            System.out.println("");
            System.out.println("🎯 Other bundles can now discover and use this service!");
            System.out.println("========================================");
            
            // Optional: Run tests if enabled via system property
            // To enable: Set -Dseat.management.test=true when starting Felix
            String testMode = System.getProperty("seat.management.test", "true"); // TEMPORARY: Always run tests
            if ("true".equalsIgnoreCase(testMode)) {
                System.out.println("\n🧪 TEST MODE ENABLED - Running test suite...\n");
                runTests();
            } else {
                System.out.println("\n💡 TIP: To run tests, set system property: -Dseat.management.test=true");
                System.out.println("   Or use SeatManagementClient from another bundle or Felix console");
            }
        } catch (Exception e) {
            System.err.println("❌ ERROR: Failed to start Seat Management Bundle!");
            System.err.println("   Error: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw to let OSGi know the bundle failed to start
        }
    }
    
    /**
     * Run test suite using SeatManagementClient
     */
    private void runTests() {
        try {
            SeatManagementClient client = new SeatManagementClient(bundleContext);
            client.runAllTests();
        } catch (Exception e) {
            System.err.println("❌ Error running tests: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Called when the bundle is stopped
     * This is where we clean up and unregister our services
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        System.out.println("========================================");
        System.out.println("🛑 Seat Management Bundle STOPPING...");
        System.out.println("========================================");
        
        // Unregister service (though OSGi does this automatically)
        if (serviceRegistration != null) {
            serviceRegistration.unregister();
            System.out.println("✅ SeatManagementService unregistered");
        }
        
        System.out.println("👋 Seat Management Bundle stopped gracefully");
        System.out.println("========================================");
    }
}
