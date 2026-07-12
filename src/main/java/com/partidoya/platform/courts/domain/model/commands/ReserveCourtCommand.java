package com.partidoya.platform.courts.domain.model.commands;

import com.partidoya.platform.courts.domain.model.valueobjects.CourtId;
import com.partidoya.platform.iam.domain.model.valueobjects.UserId;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReserveCourtCommand(
        CourtId courtId,
        UserId userId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        String paymentMethod,
        String culqiToken,
        String payerEmail,
        String idempotencyKey
) {
}
