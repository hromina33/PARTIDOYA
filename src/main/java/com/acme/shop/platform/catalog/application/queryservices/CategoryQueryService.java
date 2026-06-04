package com.acme.shop.platform.catalog.application.queryservices;

import com.acme.shop.platform.catalog.domain.model.aggregates.Category;
import com.acme.shop.platform.catalog.domain.model.queries.GetCategoryByIdQuery;

import java.util.Optional;

public interface CategoryQueryService {
    Optional<Category> handle(GetCategoryByIdQuery query);
}
