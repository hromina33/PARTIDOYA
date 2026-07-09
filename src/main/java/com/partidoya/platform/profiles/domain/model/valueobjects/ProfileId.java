package com.partidoya.platform.profiles.domain.model.valueobjects;

public record ProfileId(Long value) {
    public ProfileId {
        if (value == null || value <= 0L) {
            throw new IllegalArgumentException("profileId must be a positive value");
        }
    }
}
