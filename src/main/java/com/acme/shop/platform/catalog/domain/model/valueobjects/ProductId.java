package com.acme.shop.platform.catalog.domain.model.valueobjects;

public record ProductId(Long value) {
    public ProductId {
        if (value == null || value <= 0L) {
            throw new IllegalArgumentException("productId must be a positive value");
        }
    }
}
