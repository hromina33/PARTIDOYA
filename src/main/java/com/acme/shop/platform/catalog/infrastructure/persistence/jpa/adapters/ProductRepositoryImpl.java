package com.acme.shop.platform.catalog.infrastructure.persistence.jpa.adapters;

import com.acme.shop.platform.catalog.domain.model.aggregates.Product;
import com.acme.shop.platform.catalog.domain.model.valueobjects.ProductId;
import com.acme.shop.platform.catalog.domain.model.valueobjects.Sku;
import com.acme.shop.platform.catalog.domain.repositories.ProductRepository;
import com.acme.shop.platform.catalog.infrastructure.persistence.jpa.assemblers.ProductPersistenceAssembler;
import com.acme.shop.platform.catalog.infrastructure.persistence.jpa.repositories.ProductPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductPersistenceRepository productPersistenceRepository;

    public ProductRepositoryImpl(ProductPersistenceRepository productPersistenceRepository) {
        this.productPersistenceRepository = productPersistenceRepository;
    }

    @Override
    public Optional<Product> findById(ProductId id) {
        return productPersistenceRepository.findById(id.value())
                .map(ProductPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Product save(Product product) {
        var saved = productPersistenceRepository.save(ProductPersistenceAssembler.toPersistenceFromDomain(product));
        return ProductPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public boolean existsBySku(Sku sku) {
        return productPersistenceRepository.existsBySku(sku);
    }
}
