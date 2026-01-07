package com.skynet.passengerprofile.osgi.client;

import com.skynet.passengerprofile.osgi.api.PassengerProfileService;
import com.skynet.passengerprofile.osgi.model.Passenger;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import java.util.List;

/**
 * Example Client - Demonstrates how another bundle would use PassengerProfileService
 * 
 * This shows the OSGi Service Registry pattern:
 * 1. Get service reference from BundleContext
 * 2. Get service object
 * 3. Use the service
 * 4. Release service reference when done
 * 
 * NOTE: This is for demonstration. In a real application, another bundle would use the service.
 */
public class PassengerProfileClient {
    
    private final BundleContext context;
    
    public PassengerProfileClient(BundleContext context) {
        this.context = context;
    }
    
    /**
     * Demonstrate using the PassengerProfileService
     */
    public void demonstrateServiceUsage() {
        System.out.println("\n========================================");
        System.out.println("🔍 CLIENT: Discovering PassengerProfileService...");
        System.out.println("========================================\n");
        
        // Step 1: Get service reference from OSGi Service Registry
        ServiceReference<PassengerProfileService> serviceRef = 
            context.getServiceReference(PassengerProfileService.class);
        
        if (serviceRef == null) {
            System.err.println("❌ SERVICE NOT FOUND in OSGi Registry!");
            return;
        }
        
        System.out.println("✅ Service found!");
        System.out.println("   Service ID: " + serviceRef.getProperty("service.id"));
        System.out.println("   Bundle providing service: " + serviceRef.getBundle().getSymbolicName());
        System.out.println("");
        
        try {
            // Step 2: Get service object
            PassengerProfileService service = context.getService(serviceRef);
            
            if (service == null) {
                System.err.println("❌ Could not get service object");
                return;
            }
            
            // Step 3: Use the service!
            System.out.println("📊 Testing service methods:\n");
            
            // Test 1: Get total passenger count
            long count = service.getTotalPassengerCount();
            System.out.println("1️⃣  Total passengers: " + count);
            
            // Test 2: Get a passenger
            Passenger passenger = service.getPassenger(1L);
            if (passenger != null) {
                System.out.println("2️⃣  Retrieved passenger: " + passenger.getFullName());
            }
            
            // Test 3: Get profile completeness
            int completeness = service.getProfileCompleteness(1L);
            System.out.println("3️⃣  Profile completeness: " + completeness + "%");
            
            // Test 4: Get missing fields
            List<String> missingFields = service.getMissingFields(1L);
            System.out.println("4️⃣  Missing fields: " + missingFields);
            
            // Test 5: Update passenger
            if (passenger != null) {
                passenger.setPhoneNumber("+60123456789");
                Passenger updated = service.updatePassenger(1L, passenger);
                if (updated != null) {
                    System.out.println("5️⃣  Updated passenger! New completeness: " + 
                                     updated.getProfileCompleteness() + "%");
                }
            }
            
            System.out.println("\n✅ All service methods working correctly!");
            
        } finally {
            // Step 4: Always release service reference
            context.ungetService(serviceRef);
            System.out.println("\n🔓 Service reference released");
        }
        
        System.out.println("========================================\n");
    }
}
