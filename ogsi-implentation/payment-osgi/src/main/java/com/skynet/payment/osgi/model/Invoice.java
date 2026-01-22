package com.skynet.payment.osgi.model;

/**
 * Model class for Invoice
 */
public class Invoice {
    private Long id;
    private Long paymentId;
    private String invoiceNumber;
    private String dateIssued;
    private Double totalAmount;
    private String customerName; // Placeholder for simplicity

    public Invoice() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }
    
    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
    
    public String getDateIssued() { return dateIssued; }
    public void setDateIssued(String dateIssued) { this.dateIssued = dateIssued; }
    
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
}
