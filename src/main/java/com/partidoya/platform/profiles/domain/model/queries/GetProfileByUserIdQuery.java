package com.partidoya.platform.profiles.domain.model.queries;

import com.partidoya.platform.iam.domain.model.valueobjects.UserId;

import java.util.Objects;

public record GetProfileByUserIdQuery(UserId userId) {
    public GetProfileByUserIdQuery {
        Objects.requireNonNull(userId, "userId cannot be null");
    }
}
