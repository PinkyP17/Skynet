package com.skynet.common.service;

import com.skynet.common.model.Reservation;

public interface IBookingService {
    Reservation createBooking(int flightId, int passengerId, String seatSelection);
    boolean cancelBooking(int bookingId);
    boolean validateBooking(int bookingId);
    String retrieveBookingPNR(int bookingId);
}
