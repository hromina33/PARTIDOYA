package com.acme.shop.platform.catalog.domain.model.valueobjects;

/**
 * Strongly typed identifier of the Category aggregate. Wrapping the {@code Long}
 * primary key in a value object prevents code from mixing up identifiers from
 * different aggregates at compile time.
 */
public record CategoryId(Long value) {
    public CategoryId {
        if (value == null || value <= 0L) {
            throw new IllegalArgumentException("categoryId must be a positive value");
        }
    }
}
