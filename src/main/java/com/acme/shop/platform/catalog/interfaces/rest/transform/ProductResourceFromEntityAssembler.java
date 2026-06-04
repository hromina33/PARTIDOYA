package com.acme.shop.platform.catalog.interfaces.rest.transform;

import com.acme.shop.platform.catalog.domain.model.aggregates.Product;
import com.acme.shop.platform.catalog.interfaces.rest.resources.ProductResource;

public class ProductResourceFromEntityAssembler {
    public static ProductResource toResourceFromEntity(Product entity) {
        return new ProductResource(
                entity.getId().value(),
                entity.getSku().code(),
                entity.getName().value(),
                entity.getDescription().value(),
                entity.getPrice().amount(),
                entity.getPrice().currency(),
                entity.getStock().value(),
                entity.getCategoryId().value());
    }
}
