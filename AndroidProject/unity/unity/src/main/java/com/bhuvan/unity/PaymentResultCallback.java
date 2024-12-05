package com.bhuvan.unity;

public interface PaymentResultCallback {
    void onPaymentSuccess(String paymentId);
    void onPaymentError(String errorMessage);
}