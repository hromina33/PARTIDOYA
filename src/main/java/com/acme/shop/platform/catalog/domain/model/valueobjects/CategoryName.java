package com.acme.shop.platform.catalog.domain.model.valueobjects;

public record CategoryName(String value) {
    private static final int MAX_LENGTH = 64;

    public CategoryName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("category name must not be null or blank");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("category name must not exceed %d characters".formatted(MAX_LENGTH));
        }
    }
}
