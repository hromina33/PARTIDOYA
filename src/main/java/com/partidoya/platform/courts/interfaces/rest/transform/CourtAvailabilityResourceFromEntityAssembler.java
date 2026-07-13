package com.partidoya.platform.courts.interfaces.rest.transform;

import com.partidoya.platform.courts.application.queryservices.CourtAvailabilityView;
import com.partidoya.platform.courts.interfaces.rest.resources.CourtAvailabilityResource;

public final class CourtAvailabilityResourceFromEntityAssembler {
    private CourtAvailabilityResourceFromEntityAssembler() {
    }

    public static CourtAvailabilityResource toResourceFromEntity(CourtAvailabilityView availability) {
        return new CourtAvailabilityResource(
                availability.id(),
                availability.courtId(),
                availability.courtName(),
                availability.date(),
                availability.allDay(),
                availability.startTime(),
                availability.endTime(),
                availability.type(),
                availability.reason(),
                availability.createdAt());
    }
}
