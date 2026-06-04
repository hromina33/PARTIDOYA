package com.acme.shop.platform.catalog.domain.repositories;

import com.acme.shop.platform.catalog.domain.model.aggregates.Product;
import com.acme.shop.platform.catalog.domain.model.valueobjects.ProductId;
import com.acme.shop.platform.catalog.domain.model.valueobjects.Sku;

import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(ProductId id);
    Product save(Product product);
    boolean existsBySku(Sku sku);
}
