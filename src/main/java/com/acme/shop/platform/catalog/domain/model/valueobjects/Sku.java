package com.acme.shop.platform.catalog.domain.model.valueobjects;

import java.util.regex.Pattern;

public record Sku(String code) {
    private static final Pattern PATTERN = Pattern.compile("^[A-Z0-9-]{3,32}$");

    public Sku {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("sku must not be null or blank");
        }
        if (!PATTERN.matcher(code).matches()) {
            throw new IllegalArgumentException("sku must be 3-32 uppercase alphanumeric characters or dashes");
        }
    }
}
