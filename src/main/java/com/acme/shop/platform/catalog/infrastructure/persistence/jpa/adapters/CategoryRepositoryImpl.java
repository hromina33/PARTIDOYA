package com.acme.shop.platform.catalog.infrastructure.persistence.jpa.adapters;

import com.acme.shop.platform.catalog.domain.model.aggregates.Category;
import com.acme.shop.platform.catalog.domain.model.valueobjects.CategoryId;
import com.acme.shop.platform.catalog.domain.model.valueobjects.CategoryName;
import com.acme.shop.platform.catalog.domain.repositories.CategoryRepository;
import com.acme.shop.platform.catalog.infrastructure.persistence.jpa.assemblers.CategoryPersistenceAssembler;
import com.acme.shop.platform.catalog.infrastructure.persistence.jpa.repositories.CategoryPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Adapter for the {@link CategoryRepository} port. Translates between the
 * domain {@code Category} aggregate and the JPA persistence entity.
 */
@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryPersistenceRepository categoryPersistenceRepository;

    public CategoryRepositoryImpl(CategoryPersistenceRepository categoryPersistenceRepository) {
        this.categoryPersistenceRepository = categoryPersistenceRepository;
    }

    @Override
    public Optional<Category> findById(CategoryId id) {
        return categoryPersistenceRepository.findById(id.value())
                .map(CategoryPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Category save(Category category) {
        var categoryPersistenceEntity = CategoryPersistenceAssembler.toPersistenceFromDomain(category);
        var saved = categoryPersistenceRepository.save(categoryPersistenceEntity);
        return CategoryPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public boolean existsById(CategoryId id) {
        return categoryPersistenceRepository.existsById(id.value());
    }

    @Override
    public boolean existsByName(CategoryName name) {
        return categoryPersistenceRepository.existsByName(name.value());
    }
}
