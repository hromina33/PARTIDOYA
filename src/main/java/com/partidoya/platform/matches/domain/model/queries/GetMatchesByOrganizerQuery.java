package com.partidoya.platform.matches.domain.model.queries;

import com.partidoya.platform.iam.domain.model.valueobjects.UserId;

import java.util.Objects;

public record GetMatchesByOrganizerQuery(UserId organizerId) {
    public GetMatchesByOrganizerQuery {
        Objects.requireNonNull(organizerId, "organizerId must not be null");
    }
}
