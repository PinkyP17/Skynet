package com.skynet.booking.osgi.api;

import com.skynet.booking.osgi.model.Booking;
import java.util.List;

public interface BookingService {
    Booking createBooking(int flightId, int accountId);
    boolean cancelBooking(int bookingId);
    Booking getBookingByPnr(String pnr);
    List<Booking> getBookingsByAccount(int accountId);
}
