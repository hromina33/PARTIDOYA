package com.partidoya.platform.courts.application.queryservices;

import com.partidoya.platform.courts.domain.model.aggregates.Court;
import com.partidoya.platform.courts.domain.model.queries.GetCourtByIdQuery;
import com.partidoya.platform.courts.domain.model.queries.GetManagedAvailabilityQuery;
import com.partidoya.platform.courts.domain.model.queries.GetManagedCourtsQuery;
import com.partidoya.platform.courts.domain.model.queries.GetManagedCourtReportQuery;
import com.partidoya.platform.courts.domain.model.queries.GetManagedReservationsQuery;
import com.partidoya.platform.courts.domain.model.queries.GetReservableSchedulesQuery;
import com.partidoya.platform.courts.domain.model.queries.SearchPublishedCourtsQuery;

import java.util.List;
import java.util.Optional;

public interface CourtQueryService {
    List<Court> handle(SearchPublishedCourtsQuery query);
    Optional<Court> handle(GetCourtByIdQuery query);
    List<Court> handle(GetManagedCourtsQuery query);
    List<ManagedReservationView> handle(GetManagedReservationsQuery query);
    List<CourtAvailabilityView> handle(GetManagedAvailabilityQuery query);
    List<String> handle(GetReservableSchedulesQuery query);
    CourtReportView handle(GetManagedCourtReportQuery query);
}
