package com.partidoya.platform.courts.interfaces.rest.resources;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReserveCourtResource(
        Long userId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        String paymentMethod
) {
}
