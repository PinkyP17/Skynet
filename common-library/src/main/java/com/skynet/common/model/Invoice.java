package com.skynet.common.model;

import java.time.LocalDate;

public class Invoice {
    private int id;
    private Payment payment;
    private LocalDate issueDate;
    private String pdfUrl;

    public Invoice() {
    }

    public Invoice(int id, Payment payment, LocalDate issueDate, String pdfUrl) {
        this.id = id;
        this.payment = payment;
        this.issueDate = issueDate;
        this.pdfUrl = pdfUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }
}