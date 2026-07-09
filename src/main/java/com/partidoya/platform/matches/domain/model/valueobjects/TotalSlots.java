package com.partidoya.platform.matches.domain.model.valueobjects;

public record TotalSlots(int value) {
    public TotalSlots {
        if (value < 2) {
            throw new IllegalArgumentException("totalSlots must be at least 2");
        }
    }
}
