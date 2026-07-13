package com.partidoya.platform.courts.domain.model.queries;

import com.partidoya.platform.courts.domain.model.valueobjects.CourtId;
import com.partidoya.platform.iam.domain.model.valueobjects.UserId;

import java.time.LocalDate;

public record GetManagedAvailabilityQuery(
        UserId ownerId,
        CourtId courtId,
        LocalDate from,
        LocalDate to
) {
}
