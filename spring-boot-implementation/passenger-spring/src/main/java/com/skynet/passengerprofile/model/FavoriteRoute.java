package com.skynet.passengerprofile.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * FavoriteRoute Entity - represents saved favorite flight routes
 *
 * Maps to the 'favorite_routes' table in the database
 */
@Entity
@Table(name = "favorite_routes")
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
    private String departureAirport;

    @Column(name = "arrival_airport", nullable = false)
    private String arrivalAirport;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "created_date")
    private String createdDate;

    // Constructors
    public FavoriteRoute() {}

    public FavoriteRoute(Long id, Long passengerId, String departureAirport,
                         String arrivalAirport, String nickname, String createdDate) {
        this.id = id;
        this.passengerId = passengerId;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.nickname = nickname;
        this.createdDate = createdDate;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPassengerId() { return passengerId; }
    public void setPassengerId(Long passengerId) { this.passengerId = passengerId; }

    public Passenger getPassenger() { return passenger; }
    public void setPassenger(Passenger passenger) { this.passenger = passenger; }

    public String getDepartureAirport() { return departureAirport; }
    public void setDepartureAirport(String departureAirport) { this.departureAirport = departureAirport; }

    public String getArrivalAirport() { return arrivalAirport; }
    public void setArrivalAirport(String arrivalAirport) { this.arrivalAirport = arrivalAirport; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }

    // Helper methods
    public String getRouteDescription() {
        return departureAirport + " → " + arrivalAirport;
    }

    public String getDisplayName() {
        if (nickname != null && !nickname.isEmpty()) {
            return nickname + " (" + getRouteDescription() + ")";
        }
        return getRouteDescription();
    }

    public boolean matchesRoute(String departure, String arrival) {
        return this.departureAirport.equalsIgnoreCase(departure)
                && this.arrivalAirport.equalsIgnoreCase(arrival);
    }
}
