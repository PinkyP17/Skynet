package com.skynet.passengerprofile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Passenger Profile Service - Main Application Class
 * 
 * This is a Spring Boot microservice for managing passenger profiles
 * in the Skynet Flight Booking System.
 * 
 * Module Functionalities:
 * 1. Manage Personal Details
 * 2. Manage Saved Favorites
 * 3. Upload Travel Documents
 * 4. Manage Loyalty Points
 * 
 * Enhancements:
 * 1. Profile Completeness Meter
 * 2. Loyalty Tier Visualizer
 * 3. Document Expiry Alerts
 * 
 * @author Your Name
 * @version 1.0.0
 */
@SpringBootApplication
public class PassengerProfileApplication {

    public static void main(String[] args) {
        SpringApplication.run(PassengerProfileApplication.class, args);
        System.out.println("\n===========================================");
        System.out.println("Passenger Profile Service Started Successfully!");
        System.out.println("API Base URL: http://localhost:8081/api/passenger-profile");
        System.out.println("===========================================\n");
    }
}
