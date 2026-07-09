package com.partidoya.platform.profiles.domain.model.queries;

import com.partidoya.platform.profiles.domain.model.valueobjects.ProfileId;

import java.util.Objects;

public record GetProfileByIdQuery(ProfileId profileId) {
    public GetProfileByIdQuery {
        Objects.requireNonNull(profileId, "profileId cannot be null");
    }
}
