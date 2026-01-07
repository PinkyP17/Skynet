package com.skynet.passengerprofile.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * FavoriteRoute Entity - represents saved favorite flight routes
 * 
 * Maps to the 'favorite_routes' table in the database
 */
@Entity
@Table(name = "favorite_routes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_passenger", insertable = false, updatable = false)
    private Long passengerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_passenger", nullable = false)
    @JsonIgnore
    private Passenger passenger;

    @Column(name = "departure_airport", nullable = false)
    private String departureAirport; // e.g., "KLIA", "JFK"

    @Column(name = "arrival_airport", nullable = false)
    private String arrivalAirport; // e.g., "SIN", "LHR"

    @Column(name = "nickname")
    private String nickname; // e.g., "Weekend Gateway", "Work Trip"

    @Column(name = "created_date")
    private String createdDate;

    // Helper methods

    /**
     * Get route description
     * @return formatted route string
     */
    public String getRouteDescription() {
        return departureAirport + " → " + arrivalAirport;
    }

    /**
     * Get display name (nickname or route)
     * @return nickname if exists, otherwise route description
     */
    public String getDisplayName() {
        if (nickname != null && !nickname.isEmpty()) {
            return nickname + " (" + getRouteDescription() + ")";
        }
        return getRouteDescription();
    }

    /**
     * Check if this route matches another route
     * @param departure departure airport code
     * @param arrival arrival airport code
     * @return true if matches
     */
    public boolean matchesRoute(String departure, String arrival) {
        return this.departureAirport.equalsIgnoreCase(departure) 
            && this.arrivalAirport.equalsIgnoreCase(arrival);
    }
}
