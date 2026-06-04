package com.acme.shop.platform.catalog.interfaces.rest.resources;

import java.math.BigDecimal;

public record CreateProductResource(
        String sku,
        String name,
        String description,
        BigDecimal price,
        String currency,
        int stock,
        Long categoryId) {
}
