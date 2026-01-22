package com.skynet.payment.osgi.model;

/**
 * Model class for Payment
 */
public class Payment {
    private Long id;
    private Double amount;
    private String currency; // USD, EUR, etc.
    private String method; // CREDIT_CARD, PAYPAL, etc.
    private String status; // PENDING, COMPLETED, REFUNDED
    private String transactionDate;
    private Long bookingId;

    public Payment() {}

    public Payment(Double amount, String currency, String method, Long bookingId) {
        this.amount = amount;
        this.currency = currency;
        this.method = method;
        this.bookingId = bookingId;
        this.status = "PENDING";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getTransactionDate() { return transactionDate; }
    public void setTransactionDate(String transactionDate) { this.transactionDate = transactionDate; }
    
    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
}
