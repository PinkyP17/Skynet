package com.skynet.common.model;

public class Reservation {
    private int id;
    private Flight flight;
    private Account account;
    private Seat seat;
    private int nbrLuggages;
    private double weight;
    private String status;

    public Reservation() {
    }

    public Reservation(int id, Flight flight, Account account, Seat seat, int nbrLuggages, double weight, String status) {
        this.id = id;
        this.flight = flight;
        this.account = account;
        this.seat = seat;
        this.nbrLuggages = nbrLuggages;
        this.weight = weight;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
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
}