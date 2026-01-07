package com.skynet.common.service;

import com.skynet.common.model.Flight;
import com.skynet.common.model.FlightStatus;

import java.time.LocalDate;
import java.util.List;

public interface IFlightCatalogService {
    List<Flight> searchFlights(String origin, String destination, LocalDate date);
    Flight getFlightDetails(int flightId);
    FlightStatus getFlightStatus(int flightId);
}
