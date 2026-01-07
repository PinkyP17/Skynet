package com.skynet.booking.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "flight_id", nullable = false)
    private int flightId;

    @Column(name = "account_id", nullable = false)
    private int accountId;

    @Column(name = "seat_id")
    private int seatId; // Nullable if seat not selected yet

    @Column(name = "nbr_luggages")
    private int nbrLuggages;

    @Column(name = "weight")
    private double weight;

    @Column(name = "status", nullable = false)
    private String status; // BOOKED, CANCELLED

    @Column(name = "pnr", unique = true, length = 10)
    private String pnr;

    @Column(name = "reservation_date")
    private LocalDateTime reservationDate;

    public BookingEntity() {
    }

    public BookingEntity(Integer id, int flightId, int accountId, int seatId, int nbrLuggages, double weight, String status, String pnr, LocalDateTime reservationDate) {
        this.id = id;
        this.flightId = flightId;
        this.accountId = accountId;
        this.seatId = seatId;
        this.nbrLuggages = nbrLuggages;
        this.weight = weight;
        this.status = status;
        this.pnr = pnr;
        this.reservationDate = reservationDate;
    }

    @PrePersist
    protected void onCreate() {
        if (reservationDate == null) {
            reservationDate = LocalDateTime.now();
        }
        if (status == null) {
            status = "BOOKED";
        }
    }

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public int getNbrLuggages() {
        return nbrLuggages;
    }

    public void setNbrLuggages(int nbrLuggages) {
        this.nbrLuggages = nbrLuggages;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }
}