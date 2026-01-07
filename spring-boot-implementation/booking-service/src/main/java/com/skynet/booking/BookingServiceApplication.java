package com.skynet.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingServiceApplication.class, args);
        System.out.println("\n===========================================================");
        System.out.println("   BOOKING SERVICE STARTED SUCCESSFULLY");
        System.out.println("   API Base URL: http://localhost:8082/api/booking");
        System.out.println("===========================================================\n");
    }

}
