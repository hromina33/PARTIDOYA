package com.partidoya.platform.courts.domain.services;

import java.math.BigDecimal;

public record PaymentRequest(Long reservationId, BigDecimal amount, String method) {
}
