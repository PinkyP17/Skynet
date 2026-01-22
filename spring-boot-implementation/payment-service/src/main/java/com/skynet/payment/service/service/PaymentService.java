package com.skynet.payment.service.service;

import com.skynet.payment.service.model.PaymentEntity;
import com.skynet.payment.service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentEntity processPayment(PaymentEntity payment) {
        // Here you would integrate with Stripe/PayPal etc.
        // For now, we assume success
        payment.setStatus("COMPLETED");
        return paymentRepository.save(payment);
    }

    public PaymentEntity getPayment(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }



    public boolean processRefund(Long id) {
        Optional<PaymentEntity> optionalPayment = paymentRepository.findById(id);
        if (optionalPayment.isPresent()) {
            PaymentEntity payment = optionalPayment.get();
            if ("COMPLETED".equals(payment.getStatus())) {
                payment.setStatus("REFUNDED");
                paymentRepository.save(payment);
                return true;
            }
        }
        return false;
    }

    public List<PaymentEntity> getAllPayments() {
        return paymentRepository.findAll();
    }
    
    // Invoice generation logic
    public byte[] generateInvoicePdf(Long paymentId) throws java.io.IOException {
         Optional<PaymentEntity> paymentOpt = paymentRepository.findById(paymentId);
         if (paymentOpt.isEmpty()) {
             return null;
         }
         PaymentEntity payment = paymentOpt.get();
         
         try (java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream()) {
             com.lowagie.text.Document document = new com.lowagie.text.Document();
             com.lowagie.text.pdf.PdfWriter.getInstance(document, out);
             
             document.open();
             
             com.lowagie.text.Font titleFont = com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA_BOLD, 18);
             com.lowagie.text.Font normalFont = com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 12);
             
             document.add(new com.lowagie.text.Paragraph("Skynet Flight Booking - Invoice", titleFont));
             document.add(new com.lowagie.text.Paragraph("------------------------------------------------", normalFont));
             document.add(new com.lowagie.text.Paragraph("Invoice Number: INV-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase(), normalFont));
             document.add(new com.lowagie.text.Paragraph("Date: " + java.time.format.DateTimeFormatter.ISO_DATE_TIME.format(payment.getTransactionDate()), normalFont));
             document.add(new com.lowagie.text.Paragraph(" ", normalFont));
             
             document.add(new com.lowagie.text.Paragraph("Payment Details:", titleFont));
             document.add(new com.lowagie.text.Paragraph("Payment ID: " + payment.getId(), normalFont));
             document.add(new com.lowagie.text.Paragraph("Amount: " + payment.getAmount() + " " + payment.getCurrency(), normalFont));
             document.add(new com.lowagie.text.Paragraph("Method: " + payment.getMethod(), normalFont));
             document.add(new com.lowagie.text.Paragraph("Status: " + payment.getStatus(), normalFont));
             
             document.add(new com.lowagie.text.Paragraph(" ", normalFont));
             document.add(new com.lowagie.text.Paragraph("Thank you for flying with Skynet!", normalFont));
             
             document.close();
             return out.toByteArray();
         }
    }
}
