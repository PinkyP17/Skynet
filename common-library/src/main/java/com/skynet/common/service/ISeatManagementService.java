package com.skynet.common.service;

import com.skynet.common.model.Seat;

import java.util.List;

public interface ISeatManagementService {
    List<Seat> getSeatMap(int flightId);
    boolean reserveSeat(int flightId, String seatNumber);
    boolean releaseSeat(int reservationId);
    boolean checkAvailability(int flightId, String seatNumber);
}
