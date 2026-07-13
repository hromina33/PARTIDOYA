package com.partidoya.platform.courts.interfaces.rest.resources;

import java.time.LocalDate;
import java.time.LocalTime;

public record SaveCourtAvailabilityResource(
        Long courtId,
        LocalDate date,
        boolean allDay,
        LocalTime startTime,
        LocalTime endTime,
        String type,
        String reason
) {
}
