package com.acme.shop.platform.catalog.domain.model.valueobjects;

public record CategoryDescription(String value) {
    private static final int MAX_LENGTH = 1000;

    public CategoryDescription {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("category description must not be null or blank");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("category description must not exceed %d characters".formatted(MAX_LENGTH));
        }
    }
}
