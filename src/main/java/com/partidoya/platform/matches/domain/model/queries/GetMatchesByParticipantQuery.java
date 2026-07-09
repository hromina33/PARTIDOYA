package com.partidoya.platform.matches.domain.model.queries;

import com.partidoya.platform.iam.domain.model.valueobjects.UserId;

import java.util.Objects;

public record GetMatchesByParticipantQuery(UserId participantId) {
    public GetMatchesByParticipantQuery {
        Objects.requireNonNull(participantId, "participantId must not be null");
    }
}
