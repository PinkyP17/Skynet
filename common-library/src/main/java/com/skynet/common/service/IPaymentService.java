package com.skynet.common.service;

import com.skynet.common.model.Invoice;
import com.skynet.common.model.Payment;
import com.skynet.common.model.CreditCard;

public interface IPaymentService {
    Payment processPayment(int bookingId, CreditCard paymentDetails);
    Invoice generateInvoice(int bookingId);
    boolean processRefund(int paymentId);
    boolean applyPromoCode(int bookingId, String promoCode);
}
