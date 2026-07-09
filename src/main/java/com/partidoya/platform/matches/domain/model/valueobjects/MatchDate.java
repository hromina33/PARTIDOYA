package com.partidoya.platform.matches.domain.model.valueobjects;

import java.time.LocalDateTime;
import java.util.Objects;

public record MatchDate(LocalDateTime value) {
    public MatchDate {
        Objects.requireNonNull(value, "matchDate must not be null");
    }
}
