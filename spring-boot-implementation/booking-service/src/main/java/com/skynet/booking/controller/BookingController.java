package com.skynet.booking.controller;

import com.skynet.booking.dto.CreateBookingRequest;
import com.skynet.common.model.Reservation;
import com.skynet.common.service.IBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    private final IBookingService bookingService;

    @Autowired
    public BookingController(IBookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<Reservation> createBooking(@RequestBody CreateBookingRequest request) {
        Reservation reservation = bookingService.createBooking(
                request.getFlightId(),
                request.getPassengerId(),
                request.getSeatSelection()
        );
        return ResponseEntity.ok(reservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelBooking(@PathVariable int id) {
        boolean cancelled = bookingService.cancelBooking(id);
        if (cancelled) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/validate/{id}")
    public ResponseEntity<Boolean> validateBooking(@PathVariable int id) {
        return ResponseEntity.ok(bookingService.validateBooking(id));
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }
}