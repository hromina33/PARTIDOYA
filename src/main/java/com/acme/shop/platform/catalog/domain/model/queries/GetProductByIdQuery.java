package com.acme.shop.platform.catalog.domain.model.queries;

import com.acme.shop.platform.catalog.domain.model.valueobjects.ProductId;

import java.util.Objects;

public record GetProductByIdQuery(ProductId productId) {
    public GetProductByIdQuery {
        Objects.requireNonNull(productId, "productId cannot be null");
    }
}
