package com.partidoya.platform.matches.domain.model.queries;

import com.partidoya.platform.matches.domain.model.valueobjects.MatchId;

import java.util.Objects;

public record GetMatchByIdQuery(MatchId matchId) {
    public GetMatchByIdQuery {
        Objects.requireNonNull(matchId, "matchId cannot be null");
    }
}
