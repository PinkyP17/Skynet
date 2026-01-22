package com.skynet.seatmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Seat Management Service - Spring Boot Application
 * 
 * This is the main entry point for the Seat Management microservice.
 * It provides REST API endpoints for seat management functionality.
 */
@SpringBootApplication
public class SeatManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeatManagementApplication.class, args);
        System.out.println("========================================");
        System.out.println("🚀 Seat Management Service STARTED");
        System.out.println("========================================");
        System.out.println("📋 Available Endpoints:");
        System.out.println("   GET  /api/seat-management/seats");
        System.out.println("   GET  /api/seat-management/seats/{id}");
        System.out.println("   GET  /api/seat-management/seats/type/{type}");
        System.out.println("   GET  /api/seat-management/seats/search?pattern=A5");
        System.out.println("   GET  /api/seat-management/seats/filter/type?type=First");
        System.out.println("   GET  /api/seat-management/seats/stats?flightId=1");
        System.out.println("   ... and more");
        System.out.println("========================================");
    }
}
