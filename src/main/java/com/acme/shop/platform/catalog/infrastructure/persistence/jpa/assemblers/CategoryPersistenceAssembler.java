package com.acme.shop.platform.catalog.infrastructure.persistence.jpa.assemblers;

import com.acme.shop.platform.catalog.domain.model.aggregates.Category;
import com.acme.shop.platform.catalog.domain.model.valueobjects.CategoryDescription;
import com.acme.shop.platform.catalog.domain.model.valueobjects.CategoryId;
import com.acme.shop.platform.catalog.domain.model.valueobjects.CategoryName;
import com.acme.shop.platform.catalog.infrastructure.persistence.jpa.entities.CategoryPersistenceEntity;

/**
 * Bridges the {@code Category} domain aggregate (pure POJO with value objects)
 * and the JPA-managed {@code CategoryPersistenceEntity} (primitives).
 */
public final class CategoryPersistenceAssembler {

    private CategoryPersistenceAssembler() {
    }

    public static Category toDomainFromPersistence(CategoryPersistenceEntity entity) {
        if (entity == null) return null;
        return new Category(
                new CategoryId(entity.getId()),
                new CategoryName(entity.getName()),
                new CategoryDescription(entity.getDescription()));
    }

    public static CategoryPersistenceEntity toPersistenceFromDomain(Category category) {
        if (category == null) return null;
        var entity = new CategoryPersistenceEntity();
        if (category.getId() != null) {
            entity.setId(category.getId().value());
        }
        entity.setName(category.getName().value());
        entity.setDescription(category.getDescription().value());
        return entity;
    }
}
