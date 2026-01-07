package com.skynet.common.model;

import java.time.LocalDateTime;

public class Payment {
    private int id;
    private Reservation reservation;
    private double amount;
    private String status;
    private LocalDateTime paymentDate;
    private String transactionId;

    public Payment() {
    }

    public Payment(int id, Reservation reservation, double amount, String status, LocalDateTime paymentDate, String transactionId) {
        this.id = id;
        this.reservation = reservation;
        this.amount = amount;
        this.status = status;
        this.paymentDate = paymentDate;
        this.transactionId = transactionId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}