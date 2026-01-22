package com.skynet.payment.service.controller;

import com.skynet.payment.service.model.PaymentEntity;
import com.skynet.payment.service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<PaymentEntity> processPayment(@RequestBody PaymentEntity payment) {
        return ResponseEntity.ok(paymentService.processPayment(payment));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentEntity> getPayment(@PathVariable Long id) {
        PaymentEntity payment = paymentService.getPayment(id);
        if (payment != null) {
            return ResponseEntity.ok(payment);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/refund/{id}")
    public ResponseEntity<String> processRefund(@PathVariable Long id) {
        if (paymentService.processRefund(id)) {
            return ResponseEntity.ok("Refund processed successfully");
        }
        return ResponseEntity.badRequest().body("Refund failed or payment not found");
    }

    @GetMapping("/invoice/{id}")
    public ResponseEntity<byte[]> generateInvoice(@PathVariable Long id) {
        try {
            byte[] pdfBytes = paymentService.generateInvoicePdf(id);
            if (pdfBytes != null) {
                 return ResponseEntity.ok()
                         .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice_" + id + ".pdf")
                         .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                         .body(pdfBytes);
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/promo/apply")
    public ResponseEntity<Map<String, Object>> applyPromo(@RequestBody Map<String, Object> request) {
        String code = (String) request.get("code");
        Double amount = Double.valueOf(request.get("amount").toString());
        
        double newAmount = amount;
        if ("SKYNET2024".equalsIgnoreCase(code)) {
            newAmount = amount * 0.90;
        } else if ("WELCOME50".equalsIgnoreCase(code)) {
            newAmount = Math.max(0, amount - 50.0);
        }
        
        return ResponseEntity.ok(Map.of(
            "originalAmount", amount,
            "discountedAmount", newAmount,
            "code", code
        ));
    }

    @GetMapping("/history")
    public ResponseEntity<List<PaymentEntity>> getHistory() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }
}
