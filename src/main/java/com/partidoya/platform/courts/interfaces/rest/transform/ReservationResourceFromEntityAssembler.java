package com.partidoya.platform.courts.interfaces.rest.transform;

import com.partidoya.platform.courts.domain.model.aggregates.Reservation;
import com.partidoya.platform.courts.interfaces.rest.resources.ReservationResource;

public final class ReservationResourceFromEntityAssembler {
    private ReservationResourceFromEntityAssembler() {
    }

    public static ReservationResource toResourceFromEntity(Reservation reservation) {
        return new ReservationResource(
                reservation.getId() != null ? reservation.getId().value() : null,
                reservation.getUserId().value(),
                reservation.getCourtId().value(),
                reservation.getDate(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getPrice(),
                reservation.getCurrency(),
                reservation.getProviderReference(),
                reservation.getStatus().name(),
                reservation.getPaymentStatus().name(),
                reservation.getCreatedAt());
    }
}
