package com.acme.shop.platform.catalog.domain.model.queries;

import com.acme.shop.platform.catalog.domain.model.valueobjects.CategoryId;

import java.util.Objects;

public record GetCategoryByIdQuery(CategoryId categoryId) {
    public GetCategoryByIdQuery {
        Objects.requireNonNull(categoryId, "categoryId cannot be null");
    }
}
