package com.skynet.booking.dto;

public class CreateBookingRequest {
    private int flightId;
    private int passengerId;
    private String seatSelection;

    public CreateBookingRequest() {
    }

    public CreateBookingRequest(int flightId, int passengerId, String seatSelection) {
        this.flightId = flightId;
        this.passengerId = passengerId;
        this.seatSelection = seatSelection;
    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public String getSeatSelection() {
        return seatSelection;
    }

    public void setSeatSelection(String seatSelection) {
        this.seatSelection = seatSelection;
    }
}