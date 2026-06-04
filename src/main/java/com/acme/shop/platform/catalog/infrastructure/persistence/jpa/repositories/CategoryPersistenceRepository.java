package com.acme.shop.platform.catalog.infrastructure.persistence.jpa.repositories;

import com.acme.shop.platform.catalog.infrastructure.persistence.jpa.entities.CategoryPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryPersistenceRepository extends JpaRepository<CategoryPersistenceEntity, Long> {
    boolean existsByName(String name);
}
