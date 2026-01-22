package com.skynet.payment.service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    private String currency; // USD, EUR
    private String method; // CREDIT_CARD, PAYPAL
    private String status; // PENDING, COMPLETED, REFUNDED
    
    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;
    
    @Column(name = "booking_id")
    private Long bookingId;
    
    @PrePersist
    protected void onCreate() {
        if (transactionDate == null) {
            transactionDate = LocalDateTime.now();
        }
        if (status == null) {
            status = "PENDING";
        }
    }
}
