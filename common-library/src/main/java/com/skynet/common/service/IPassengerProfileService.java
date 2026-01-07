package com.skynet.common.service;

import com.skynet.common.model.Passenger;

public interface IPassengerProfileService {
    Passenger getPassengerProfile(int passengerId);
    boolean updatePassengerDetails(Passenger passenger);
    void manageLoyaltyAccount(int passengerId); // Return type not specified
    boolean validateTravelDocuments(int passengerId);
}
