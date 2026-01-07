package com.skynet.booking.osgi.impl;

import com.skynet.booking.osgi.api.BookingService;
import com.skynet.booking.osgi.model.Booking;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BookingServiceImpl implements BookingService {

    private final List<Booking> bookings = new ArrayList<>();
    private int idCounter = 1;

    public BookingServiceImpl() {
        System.out.println("[BookingService] OSGi Service Implementation initialized");
    }

    @Override
    public Booking createBooking(int flightId, int accountId) {
        String pnr = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        Booking booking = new Booking(idCounter++, flightId, accountId, pnr, "BOOKED");
        bookings.add(booking);
        System.out.println("[BookingService] Created booking: " + pnr);
        return booking;
    }

    @Override
    public boolean cancelBooking(int bookingId) {
        for (Booking b : bookings) {
            if (b.getId() == bookingId) {
                b.setStatus("CANCELLED");
                System.out.println("[BookingService] Cancelled booking: " + b.getPnr());
                return true;
            }
        }
        return false;
    }

    @Override
    public Booking getBookingByPnr(String pnr) {
        return bookings.stream()
                .filter(b -> b.getPnr().equalsIgnoreCase(pnr))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Booking> getBookingsByAccount(int accountId) {
        return bookings.stream()
                .filter(b -> b.getAccountId() == accountId)
                .collect(Collectors.toList());
    }
}
