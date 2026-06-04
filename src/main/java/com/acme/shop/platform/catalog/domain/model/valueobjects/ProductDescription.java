package com.acme.shop.platform.catalog.domain.model.valueobjects;

public record ProductDescription(String value) {
    private static final int MAX_LENGTH = 2000;

    public ProductDescription {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("product description must not be null or blank");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("product description must not exceed %d characters".formatted(MAX_LENGTH));
        }
    }
}
