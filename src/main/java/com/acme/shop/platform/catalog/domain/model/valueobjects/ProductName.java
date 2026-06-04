package com.acme.shop.platform.catalog.domain.model.valueobjects;

public record ProductName(String value) {
    private static final int MAX_LENGTH = 200;

    public ProductName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("product name must not be null or blank");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("product name must not exceed %d characters".formatted(MAX_LENGTH));
        }
    }
}
