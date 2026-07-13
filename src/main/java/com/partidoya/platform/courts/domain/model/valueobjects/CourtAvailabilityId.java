package com.partidoya.platform.courts.domain.model.valueobjects;

public record CourtAvailabilityId(Long value) {
    public CourtAvailabilityId {
        if (value == null || value <= 0) throw new IllegalArgumentException("Availability id must be positive");
    }
}
