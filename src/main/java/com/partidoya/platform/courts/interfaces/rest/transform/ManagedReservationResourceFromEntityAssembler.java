package com.partidoya.platform.courts.interfaces.rest.transform;

import com.partidoya.platform.courts.application.queryservices.ManagedReservationView;
import com.partidoya.platform.courts.interfaces.rest.resources.ManagedReservationResource;

public final class ManagedReservationResourceFromEntityAssembler {
    private ManagedReservationResourceFromEntityAssembler() {
    }

    public static ManagedReservationResource toResourceFromEntity(ManagedReservationView reservation) {
        return new ManagedReservationResource(
                reservation.id(),
                reservation.userId(),
                reservation.customerName(),
                reservation.courtId(),
                reservation.courtName(),
                reservation.date(),
                reservation.startTime(),
                reservation.endTime(),
                reservation.price(),
                reservation.currency(),
                reservation.providerReference(),
                reservation.status(),
                reservation.paymentStatus(),
                reservation.createdAt());
    }
}
