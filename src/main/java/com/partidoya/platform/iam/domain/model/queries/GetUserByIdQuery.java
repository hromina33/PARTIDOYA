package com.partidoya.platform.iam.domain.model.queries;

import com.partidoya.platform.iam.domain.model.valueobjects.UserId;

import java.util.Objects;

public record GetUserByIdQuery(UserId userId) {
    public GetUserByIdQuery {
        Objects.requireNonNull(userId, "userId cannot be null");
    }
}
