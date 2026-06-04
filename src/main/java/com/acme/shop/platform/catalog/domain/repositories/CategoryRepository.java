package com.acme.shop.platform.catalog.domain.repositories;

import com.acme.shop.platform.catalog.domain.model.aggregates.Category;
import com.acme.shop.platform.catalog.domain.model.valueobjects.CategoryId;
import com.acme.shop.platform.catalog.domain.model.valueobjects.CategoryName;

import java.util.Optional;

/**
 * Category repository port — defined by the domain, implemented by infrastructure.
 */
public interface CategoryRepository {
    Optional<Category> findById(CategoryId id);
    Category save(Category category);
    boolean existsById(CategoryId id);
    boolean existsByName(CategoryName name);
}
