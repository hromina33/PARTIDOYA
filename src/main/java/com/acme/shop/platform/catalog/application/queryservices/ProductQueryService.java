package com.acme.shop.platform.catalog.application.queryservices;

import com.acme.shop.platform.catalog.domain.model.aggregates.Product;
import com.acme.shop.platform.catalog.domain.model.queries.GetProductByIdQuery;

import java.util.Optional;

public interface ProductQueryService {
    Optional<Product> handle(GetProductByIdQuery query);
}
