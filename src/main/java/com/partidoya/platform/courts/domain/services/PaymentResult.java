package com.partidoya.platform.courts.domain.services;

public record PaymentResult(boolean approved, String providerReference, String status, String currency) {
}
