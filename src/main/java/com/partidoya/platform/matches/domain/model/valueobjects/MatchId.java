package com.partidoya.platform.matches.domain.model.valueobjects;

public record MatchId(Long value) {
    public MatchId {
        if (value == null || value <= 0L) {
            throw new IllegalArgumentException("matchId must be a positive value");
        }
    }
}
