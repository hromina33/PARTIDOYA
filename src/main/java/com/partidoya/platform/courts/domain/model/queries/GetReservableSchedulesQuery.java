package com.partidoya.platform.courts.domain.model.queries;

import com.partidoya.platform.courts.domain.model.valueobjects.CourtId;

import java.time.LocalDate;

public record GetReservableSchedulesQuery(CourtId courtId, LocalDate date) {
}
