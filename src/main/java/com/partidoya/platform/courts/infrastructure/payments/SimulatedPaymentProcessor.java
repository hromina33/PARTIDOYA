package com.partidoya.platform.courts.infrastructure.payments;

import com.partidoya.platform.courts.domain.services.PaymentProcessor;
import com.partidoya.platform.courts.domain.services.PaymentRequest;
import com.partidoya.platform.courts.domain.services.PaymentResult;
import org.springframework.stereotype.Component;

@Component
public class SimulatedPaymentProcessor implements PaymentProcessor {
    @Override
    public PaymentResult process(PaymentRequest request) {
        return new PaymentResult(true, "SIM-%d".formatted(request.reservationId()));
    }
}
