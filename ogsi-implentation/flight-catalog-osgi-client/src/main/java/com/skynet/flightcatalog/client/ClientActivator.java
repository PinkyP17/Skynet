package com.skynet.flightcatalog.client;

import com.skynet.flightcatalog.osgi.api.FlightCatalogService;
import com.skynet.flightcatalog.osgi.model.Flight;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import java.util.List;

public class ClientActivator implements BundleActivator {
    
    @Override
    public void start(BundleContext context) throws Exception {
        System.out.println("\n========================================");
        System.out.println("🔌 Flight Catalog Client Starting...");
        System.out.println("========================================\n");
        
        // Wait a bit for service to be available
        Thread.sleep(1000);
        
        // Get service reference from OSGi Service Registry
        ServiceReference<FlightCatalogService> serviceRef = 
            context.getServiceReference(FlightCatalogService.class);
        
        if (serviceRef == null) {
            System.out.println("❌ ERROR: FlightCatalogService not found!");
            System.out.println("   Make sure Flight Catalog bundle is started first.");
            return;
        }
        
        // Get the actual service instance
        FlightCatalogService service = context.getService(serviceRef);
        
        if (service == null) {
            System.out.println("❌ ERROR: Could not get service instance!");
            return;
        }
        
        System.out.println("✅ Service found and retrieved!\n");
        
        // Test 1: Get all flights
        System.out.println("Test 1: Getting all flights...");
        try {
            List<Flight> flights = service.getAllFlights();
            System.out.println("✅ SUCCESS: Found " + flights.size() + " flights in database");
            if (!flights.isEmpty()) {
                Flight first = flights.get(0);
                System.out.println("   First flight: ID=" + first.getId() + 
                    ", Status=" + first.getStatus());
            }
        } catch (Exception e) {
            System.out.println("❌ FAILED: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Test 2: Get flights by status
        System.out.println("\nTest 2: Getting flights with ON_TIME status...");
        try {
            List<Flight> flights = service.getFlightsByStatus("ON_TIME");
            System.out.println("✅ SUCCESS: Found " + flights.size() + " flights with ON_TIME status");
        } catch (Exception e) {
            System.out.println("❌ FAILED: " + e.getMessage());
        }
        
        // Test 3: Sort by price
        System.out.println("\nTest 3: Sorting flights by price...");
        try {
            List<Flight> flights = service.sortFlightsByPrice(true);
            System.out.println("✅ SUCCESS: Sorted " + flights.size() + " flights by price");
            if (!flights.isEmpty()) {
                Flight cheapest = flights.get(0);
                System.out.println("   Cheapest flight: Min price = $" + cheapest.getMinPrice());
            }
        } catch (Exception e) {
            System.out.println("❌ FAILED: " + e.getMessage());
        }
        
        System.out.println("\n========================================");
        System.out.println("✅ Client tests completed!");
        System.out.println("========================================\n");
        
        // Release service
        context.ungetService(serviceRef);
    }
    
    @Override
    public void stop(BundleContext context) throws Exception {
        System.out.println("Flight Catalog Client stopped");
    }
}
