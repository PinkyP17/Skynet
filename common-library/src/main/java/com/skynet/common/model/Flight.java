package com.skynet.common.model;

import java.time.LocalDateTime;

public class Flight {
    private int id;
    private LocalDateTime depDatetime;
    private LocalDateTime arrDatetime;
    private double firstPrice;
    private double businessPrice;
    private double economyPrice;
    private double luggagePrice;
    private double weightPrice;
    
    private Airport depAirport;
    private Airport arrAirport;
    private Airline airline;
    private FlightStatus status;
    private boolean isFavorite; 

    public Flight() {
    }

    public Flight(int id, LocalDateTime depDatetime, LocalDateTime arrDatetime, double firstPrice, double businessPrice, double economyPrice, double luggagePrice, double weightPrice, Airport depAirport, Airport arrAirport, Airline airline, FlightStatus status, boolean isFavorite) {
        this.id = id;
        this.depDatetime = depDatetime;
        this.arrDatetime = arrDatetime;
        this.firstPrice = firstPrice;
        this.businessPrice = businessPrice;
        this.economyPrice = economyPrice;
        this.luggagePrice = luggagePrice;
        this.weightPrice = weightPrice;
        this.depAirport = depAirport;
        this.arrAirport = arrAirport;
        this.airline = airline;
        this.status = status;
        this.isFavorite = isFavorite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDepDatetime() {
        return depDatetime;
    }

    public void setDepDatetime(LocalDateTime depDatetime) {
        this.depDatetime = depDatetime;
    }

    public LocalDateTime getArrDatetime() {
        return arrDatetime;
    }

    public void setArrDatetime(LocalDateTime arrDatetime) {
        this.arrDatetime = arrDatetime;
    }

    public double getFirstPrice() {
        return firstPrice;
    }

    public void setFirstPrice(double firstPrice) {
        this.firstPrice = firstPrice;
    }

    public double getBusinessPrice() {
        return businessPrice;
    }

    public void setBusinessPrice(double businessPrice) {
        this.businessPrice = businessPrice;
    }

    public double getEconomyPrice() {
        return economyPrice;
    }

    public void setEconomyPrice(double economyPrice) {
        this.economyPrice = economyPrice;
    }

    public double getLuggagePrice() {
        return luggagePrice;
    }

    public void setLuggagePrice(double luggagePrice) {
        this.luggagePrice = luggagePrice;
    }

    public double getWeightPrice() {
        return weightPrice;
    }

    public void setWeightPrice(double weightPrice) {
        this.weightPrice = weightPrice;
    }

    public Airport getDepAirport() {
        return depAirport;
    }

    public void setDepAirport(Airport depAirport) {
        this.depAirport = depAirport;
    }

    public Airport getArrAirport() {
        return arrAirport;
    }

    public void setArrAirport(Airport arrAirport) {
        this.arrAirport = arrAirport;
    }

    public Airline getAirline() {
        return airline;
    }

    public void setAirline(Airline airline) {
        this.airline = airline;
    }

    public FlightStatus getStatus() {
        return status;
    }

    public void setStatus(FlightStatus status) {
        this.status = status;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}