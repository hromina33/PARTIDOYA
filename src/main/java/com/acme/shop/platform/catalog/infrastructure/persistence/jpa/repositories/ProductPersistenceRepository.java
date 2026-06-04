package com.acme.shop.platform.catalog.infrastructure.persistence.jpa.repositories;

import com.acme.shop.platform.catalog.domain.model.valueobjects.Sku;
import com.acme.shop.platform.catalog.infrastructure.persistence.jpa.entities.ProductPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPersistenceRepository extends JpaRepository<ProductPersistenceEntity, Long> {
    boolean existsBySku(Sku sku);
}
