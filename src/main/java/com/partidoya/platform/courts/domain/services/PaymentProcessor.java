package com.partidoya.platform.courts.domain.services;

public interface PaymentProcessor {
    PaymentResult process(PaymentRequest request);
}
