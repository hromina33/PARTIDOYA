package com.acme.shop.platform.catalog.interfaces.rest.transform;

import com.acme.shop.platform.catalog.domain.model.aggregates.Category;
import com.acme.shop.platform.catalog.interfaces.rest.resources.CategoryResource;

public class CategoryResourceFromEntityAssembler {
    public static CategoryResource toResourceFromEntity(Category entity) {
        return new CategoryResource(
                entity.getId().value(),
                entity.getName().value(),
                entity.getDescription().value());
    }
}
