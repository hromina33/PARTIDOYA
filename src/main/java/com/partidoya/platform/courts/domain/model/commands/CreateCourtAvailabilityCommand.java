package com.partidoya.platform.courts.domain.model.commands;

import com.partidoya.platform.courts.domain.model.valueobjects.CourtAvailabilityType;
import com.partidoya.platform.courts.domain.model.valueobjects.CourtId;
import com.partidoya.platform.iam.domain.model.valueobjects.UserId;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateCourtAvailabilityCommand(
        UserId requesterId,
        CourtId courtId,
        LocalDate date,
        boolean allDay,
        LocalTime startTime,
        LocalTime endTime,
        CourtAvailabilityType type,
        String reason
) {
}
