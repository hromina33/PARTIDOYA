package com.partidoya.platform.iam.domain.model.valueobjects;

public record HashedPassword(String value) {
    public HashedPassword {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("hashedPassword must not be null or blank");
        }
    }
}
