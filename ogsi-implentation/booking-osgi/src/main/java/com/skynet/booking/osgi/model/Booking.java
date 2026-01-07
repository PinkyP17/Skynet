package com.skynet.booking.osgi.model;

public class Booking {
    private int id;
    private int flightId;
    private int accountId;
    private String pnr;
    private String status;

    public Booking() {}

    public Booking(int id, int flightId, int accountId, String pnr, String status) {
        this.id = id;
        this.flightId = flightId;
        this.accountId = accountId;
        this.pnr = pnr;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getFlightId() { return flightId; }
    public void setFlightId(int flightId) { this.flightId = flightId; }

    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public String getPnr() { return pnr; }
    public void setPnr(String pnr) { this.pnr = pnr; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Booking [ID=" + id + ", PNR=" + pnr + ", Status=" + status + "]";
    }
}
