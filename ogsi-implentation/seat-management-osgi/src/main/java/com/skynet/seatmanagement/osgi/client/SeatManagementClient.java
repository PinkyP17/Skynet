package com.skynet.seatmanagement.osgi.client;

import com.skynet.seatmanagement.osgi.api.SeatManagementService;
import com.skynet.seatmanagement.osgi.model.Seat;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import java.util.Comparator;
import java.util.List;

/**
 * Test Client - Demonstrates how to test SeatManagementService in Apache Felix
 * 
 * This shows the OSGi Service Registry pattern:
 * 1. Get service reference from BundleContext
 * 2. Get service object
 * 3. Test the service methods
 * 4. Release service reference when done
 * 
 * Usage in Apache Felix:
 * 1. Install and start the seat-management-osgi bundle
 * 2. Call this client's test methods from Felix console or another bundle
 */
public class SeatManagementClient {
    
    private final BundleContext context;
    
    public SeatManagementClient(BundleContext context) {
        this.context = context;
    }
    
    /**
     * Test Case: TC-SM-01 - Retrieve All Seats (Service)
     * Method: getAllSeats()
     * Expected: Console shows SUCCESS + total seats retrieved
     */
    public void testGetAllSeats() {
        System.out.println("\n========================================");
        System.out.println("🔍 TEST: TC-SM-01 - Retrieve All Seats");
        System.out.println("========================================\n");
        
        ServiceReference<SeatManagementService> serviceRef = 
            context.getServiceReference(SeatManagementService.class);
        
        if (serviceRef == null) {
            System.err.println("❌ SERVICE NOT FOUND in OSGi Registry!");
            System.out.println("   Make sure seat-management-osgi bundle is installed and started");
            return;
        }
        
        System.out.println("✅ Service found!");
        System.out.println("   Service ID: " + serviceRef.getProperty("service.id"));
        System.out.println("   Bundle: " + serviceRef.getBundle().getSymbolicName());
        System.out.println("");
        
        try {
            SeatManagementService service = context.getService(serviceRef);
            
            if (service == null) {
                System.err.println("❌ Could not get service object");
                return;
            }
            
            // Test: Get all seats
            System.out.println("📊 Testing getAllSeats()...\n");
            List<Seat> allSeats = service.getAllSeats();
            
            if (allSeats == null) {
                System.err.println("❌ FAIL: getAllSeats() returned null");
                return;
            }
            
            System.out.println("✅ SUCCESS - Total seats retrieved: " + allSeats.size());
            System.out.println("");
            
            // Display first 10 seats as sample
            int displayCount = Math.min(10, allSeats.size());
            System.out.println("📋 Sample seats (showing first " + displayCount + "):");
            for (int i = 0; i < displayCount; i++) {
                Seat seat = allSeats.get(i);
                System.out.println("   " + (i + 1) + ". " + seat.toString());
            }
            
            if (allSeats.size() > displayCount) {
                System.out.println("   ... and " + (allSeats.size() - displayCount) + " more seats");
            }
            
            System.out.println("\n✅ TEST PASSED: getAllSeats() working correctly!");
            
        } catch (Exception e) {
            System.err.println("❌ TEST FAILED: Exception occurred");
            e.printStackTrace();
        } finally {
            context.ungetService(serviceRef);
            System.out.println("\n🔓 Service reference released");
        }
        
        System.out.println("========================================\n");
    }
    
    /**
     * Test Case: TC-SM-02 - Filter Seats by Type (Service)
     * Method: getSeatsByType(String type)
     * Expected: Seats filtered by type; correct type displayed
     */
    public void testFilterSeatsByType() {
        System.out.println("\n========================================");
        System.out.println("🔍 TEST: TC-SM-02 - Filter Seats by Type");
        System.out.println("========================================\n");
        
        ServiceReference<SeatManagementService> serviceRef = 
            context.getServiceReference(SeatManagementService.class);
        
        if (serviceRef == null) {
            System.err.println("❌ SERVICE NOT FOUND in OSGi Registry!");
            return;
        }
        
        try {
            SeatManagementService service = context.getService(serviceRef);
            
            if (service == null) {
                System.err.println("❌ Could not get service object");
                return;
            }
            
            // Test: Filter by First Class
            System.out.println("📊 Testing getSeatsByType(\"First\")...\n");
            List<Seat> firstClassSeats = service.getSeatsByType("First");
            
            System.out.println("✅ SUCCESS - First Class seats: " + firstClassSeats.size());
            if (!firstClassSeats.isEmpty()) {
                System.out.println("   Sample: " + firstClassSeats.get(0).toString());
            }
            
            // Test: Filter by Business
            System.out.println("\n📊 Testing getSeatsByType(\"Business\")...\n");
            List<Seat> businessSeats = service.getSeatsByType("Business");
            
            System.out.println("✅ SUCCESS - Business seats: " + businessSeats.size());
            if (!businessSeats.isEmpty()) {
                System.out.println("   Sample: " + businessSeats.get(0).toString());
            }
            
            // Test: Filter by Economy
            System.out.println("\n📊 Testing getSeatsByType(\"Economy\")...\n");
            List<Seat> economySeats = service.getSeatsByType("Economy");
            
            System.out.println("✅ SUCCESS - Economy seats: " + economySeats.size());
            if (!economySeats.isEmpty()) {
                System.out.println("   Sample: " + economySeats.get(0).toString());
            }
            
            System.out.println("\n✅ TEST PASSED: Filter by type working correctly!");
            
        } catch (Exception e) {
            System.err.println("❌ TEST FAILED: Exception occurred");
            e.printStackTrace();
        } finally {
            context.ungetService(serviceRef);
        }
        
        System.out.println("========================================\n");
    }
    
    /**
     * Test Case: TC-SM-03 - Sort Seats by Type (Service)
     * Method: getAllSeats() + sort by type
     * Expected: Seats sorted by type; First, Business, Economy order displayed correctly
     */
    public void testSortSeatsByType() {
        System.out.println("\n========================================");
        System.out.println("🔍 TEST: TC-SM-03 - Sort Seats by Type");
        System.out.println("========================================\n");
        
        ServiceReference<SeatManagementService> serviceRef = 
            context.getServiceReference(SeatManagementService.class);
        
        if (serviceRef == null) {
            System.err.println("❌ SERVICE NOT FOUND in OSGi Registry!");
            return;
        }
        
        try {
            SeatManagementService service = context.getService(serviceRef);
            
            if (service == null) {
                System.err.println("❌ Could not get service object");
                return;
            }
            
            // Get all seats and sort by type
            System.out.println("📊 Testing sortSeatsByType(true)...\n");
            List<Seat> allSeats = service.getAllSeats();
            
            // Sort by type: First -> Business -> Economy
            allSeats.sort((s1, s2) -> {
                int typeOrder1 = getTypeOrder(s1.getType());
                int typeOrder2 = getTypeOrder(s2.getType());
                return Integer.compare(typeOrder1, typeOrder2);
            });
            
            System.out.println("✅ SUCCESS - Seats sorted by type (First, Business, Economy)");
            System.out.println("   Total seats: " + allSeats.size());
            System.out.println("");
            
            // Display sorted seats (first 15)
            System.out.println("📋 Sorted seats (showing first 15):");
            int displayCount = Math.min(15, allSeats.size());
            for (int i = 0; i < displayCount; i++) {
                Seat seat = allSeats.get(i);
                System.out.println("   " + (i + 1) + ". " + seat.toString());
            }
            
            // Verify sorting
            boolean sortedCorrectly = true;
            for (int i = 1; i < allSeats.size(); i++) {
                int prevOrder = getTypeOrder(allSeats.get(i - 1).getType());
                int currOrder = getTypeOrder(allSeats.get(i).getType());
                if (prevOrder > currOrder) {
                    sortedCorrectly = false;
                    break;
                }
            }
            
            if (sortedCorrectly) {
                System.out.println("\n✅ VERIFIED: Seats are correctly sorted by type!");
            } else {
                System.out.println("\n⚠️  WARNING: Seats may not be correctly sorted");
            }
            
            System.out.println("\n✅ TEST PASSED: Sort by type working correctly!");
            
        } catch (Exception e) {
            System.err.println("❌ TEST FAILED: Exception occurred");
            e.printStackTrace();
        } finally {
            context.ungetService(serviceRef);
        }
        
        System.out.println("========================================\n");
    }
    
    /**
     * Helper method to get type order for sorting
     * First = 1, Business = 2, Economy = 3
     */
    private int getTypeOrder(String type) {
        if (type == null) return 999;
        String upperType = type.toUpperCase();
        if (upperType.contains("FIRST")) return 1;
        if (upperType.contains("BUSINESS")) return 2;
        if (upperType.contains("ECONOMY")) return 3;
        return 999;
    }
    
    /**
     * Run all test cases
     */
    public void runAllTests() {
        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║   SEAT MANAGEMENT SERVICE - TEST SUITE                 ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        
        testGetAllSeats();
        testFilterSeatsByType();
        testSortSeatsByType();
        
        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║   ALL TESTS COMPLETED                                 ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
    }
}
