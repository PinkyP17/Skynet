package com.skynet.flightcatalog.osgi;

import com.skynet.flightcatalog.osgi.api.FlightCatalogService;
import com.skynet.flightcatalog.osgi.impl.FlightCatalogServiceImpl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * Bundle Activator for Flight Catalog OSGi Bundle
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
    
    private ServiceRegistration<FlightCatalogService> serviceRegistration;
    
    /**
     * Called when the bundle is started
     * This is where we register our services in the OSGi Service Registry
     */
    @Override
    public void start(BundleContext context) throws Exception {
        System.out.println("========================================");
        System.out.println("🚀 Flight Catalog Bundle STARTING...");
        System.out.println("========================================");
        
        // Create service implementation
        FlightCatalogService service = new FlightCatalogServiceImpl();
        
        // Register service in OSGi Service Registry
        serviceRegistration = context.registerService(
            FlightCatalogService.class,
            service,
            null  // No service properties for now
        );
        
        System.out.println("✅ FlightCatalogService registered in OSGi Service Registry");
        System.out.println("   Service ID: " + serviceRegistration.getReference().getProperty("service.id"));
        System.out.println("   Bundle ID: " + context.getBundle().getBundleId());
        System.out.println("   Bundle Name: " + context.getBundle().getSymbolicName());
        System.out.println("   Bundle Version: " + context.getBundle().getVersion());
        System.out.println("");
        System.out.println("📋 Available Service Methods:");
        System.out.println("   Functionality 1: Add/Update Flights");
        System.out.println("     - createFlight(Flight flight)");
        System.out.println("     - updateFlight(Integer id, Flight flight)");
        System.out.println("     - deleteFlight(Integer id)");
        System.out.println("     - getFlightById(Integer id)");
        System.out.println("     - getAllFlights()");
        System.out.println("");
        System.out.println("   Functionality 2: Search Flights by Date/Route");
        System.out.println("     - searchFlightsByDate(LocalDateTime date)");
        System.out.println("     - searchFlightsByRoute(Integer depAirportId, Integer arrAirportId)");
        System.out.println("     - searchFlightsByRouteAndDate(...)");
        System.out.println("");
        System.out.println("   Functionality 3: Filter by Price/Duration");
        System.out.println("     - filterFlightsByMaxPrice(Double maxPrice)");
        System.out.println("     - filterFlightsByPriceRange(Double minPrice, Double maxPrice)");
        System.out.println("     - filterFlightsByMaxDuration(Long maxDurationMinutes)");
        System.out.println("     - filterFlightsByDurationRange(...)");
        System.out.println("");
        System.out.println("   Functionality 4: Update Flight Status");
        System.out.println("     - updateFlightStatus(Integer id, String status)");
        System.out.println("     - getFlightsByStatus(String status)");
        System.out.println("");
        System.out.println("   Enhancements:");
        System.out.println("     - sortFlightsByDepartureTime(boolean ascending)");
        System.out.println("     - sortFlightsByPrice(boolean ascending)");
        System.out.println("     - sortFlightsByDuration(boolean ascending)");
        System.out.println("     - isDuplicateFlight(Flight flight, Integer excludeId)");
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
        System.out.println("🛑 Flight Catalog Bundle STOPPING...");
        System.out.println("========================================");
        
        // Unregister service (though OSGi does this automatically)
        if (serviceRegistration != null) {
            serviceRegistration.unregister();
            System.out.println("✅ FlightCatalogService unregistered");
        }
        
        System.out.println("👋 Flight Catalog Bundle stopped gracefully");
        System.out.println("========================================");
    }
}
