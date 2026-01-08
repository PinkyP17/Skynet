package com.skynet.flightcatalog.osgi.test;

import com.skynet.flightcatalog.osgi.api.FlightCatalogService;
import com.skynet.flightcatalog.osgi.model.Flight;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import java.time.LocalDateTime;

/**
 * Simple test class to verify FlightCatalogService is working
 * Run this after bundle is started in Felix
 */
public class FlightCatalogServiceTest {
    
    public static void testService(BundleContext context) {
        System.out.println("\n========================================");
        System.out.println("🧪 Testing Flight Catalog Service");
        System.out.println("========================================\n");
        
        try {
            // Get service reference
            ServiceReference<FlightCatalogService> serviceRef = 
                context.getServiceReference(FlightCatalogService.class);
            
            if (serviceRef == null) {
                System.out.println("❌ ERROR: FlightCatalogService not found in Service Registry!");
                return;
            }
            
            // Get service instance
            FlightCatalogService service = context.getService(serviceRef);
            
            if (service == null) {
                System.out.println("❌ ERROR: Could not get service instance!");
                return;
            }
            
            System.out.println("✅ Service found and retrieved successfully!\n");
            
            // Test 1: Get all flights
            System.out.println("Test 1: Getting all flights...");
            try {
                var flights = service.getAllFlights();
                System.out.println("✅ SUCCESS: Retrieved " + flights.size() + " flights");
            } catch (Exception e) {
                System.out.println("❌ FAILED: " + e.getMessage());
                e.printStackTrace();
            }
            
            // Test 2: Search by date
            System.out.println("\nTest 2: Searching flights by date...");
            try {
                var flights = service.searchFlightsByDate(LocalDateTime.now());
                System.out.println("✅ SUCCESS: Found " + flights.size() + " flights for today");
            } catch (Exception e) {
                System.out.println("❌ FAILED: " + e.getMessage());
            }
            
            // Test 3: Filter by price
            System.out.println("\nTest 3: Filtering flights by max price (500.0)...");
            try {
                var flights = service.filterFlightsByMaxPrice(500.0);
                System.out.println("✅ SUCCESS: Found " + flights.size() + " flights under $500");
            } catch (Exception e) {
                System.out.println("❌ FAILED: " + e.getMessage());
            }
            
            // Test 4: Get flights by status
            System.out.println("\nTest 4: Getting flights by status (ON_TIME)...");
            try {
                var flights = service.getFlightsByStatus("ON_TIME");
                System.out.println("✅ SUCCESS: Found " + flights.size() + " flights with ON_TIME status");
            } catch (Exception e) {
                System.out.println("❌ FAILED: " + e.getMessage());
            }
            
            // Test 5: Sort by departure time
            System.out.println("\nTest 5: Sorting flights by departure time...");
            try {
                var flights = service.sortFlightsByDepartureTime(true);
                System.out.println("✅ SUCCESS: Sorted " + flights.size() + " flights by departure time");
            } catch (Exception e) {
                System.out.println("❌ FAILED: " + e.getMessage());
            }
            
            // Test 6: Sort by price
            System.out.println("\nTest 6: Sorting flights by price...");
            try {
                var flights = service.sortFlightsByPrice(true);
                System.out.println("✅ SUCCESS: Sorted " + flights.size() + " flights by price");
            } catch (Exception e) {
                System.out.println("❌ FAILED: " + e.getMessage());
            }
            
            // Test 7: Sort by duration
            System.out.println("\nTest 7: Sorting flights by duration...");
            try {
                var flights = service.sortFlightsByDuration(true);
                System.out.println("✅ SUCCESS: Sorted " + flights.size() + " flights by duration");
            } catch (Exception e) {
                System.out.println("❌ FAILED: " + e.getMessage());
            }
            
            System.out.println("\n========================================");
            System.out.println("✅ All tests completed!");
            System.out.println("========================================\n");
            
            // Release service
            context.ungetService(serviceRef);
            
        } catch (Exception e) {
            System.out.println("❌ ERROR during testing: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
