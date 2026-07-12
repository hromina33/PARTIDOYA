package com.partidoya.platform.matches.interfaces.rest.resources;

public record CreateMatchResource(
        Long organizerId,
        Long courtReservationId,
        String sport,
        String title,
        String description,
        String address,
        String matchDate,
        int totalSlots,
        Double price,
        Double latitude,
        Double longitude,
        Boolean requiresPlayerPayment,
        String yapePhone) {
}
