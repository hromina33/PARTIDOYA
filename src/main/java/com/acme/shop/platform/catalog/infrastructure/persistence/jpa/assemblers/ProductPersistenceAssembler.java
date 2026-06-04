package com.acme.shop.platform.catalog.infrastructure.persistence.jpa.assemblers;

import com.acme.shop.platform.catalog.domain.model.aggregates.Product;
import com.acme.shop.platform.catalog.domain.model.valueobjects.CategoryId;
import com.acme.shop.platform.catalog.domain.model.valueobjects.Money;
import com.acme.shop.platform.catalog.domain.model.valueobjects.ProductDescription;
import com.acme.shop.platform.catalog.domain.model.valueobjects.ProductId;
import com.acme.shop.platform.catalog.domain.model.valueobjects.ProductName;
import com.acme.shop.platform.catalog.domain.model.valueobjects.Stock;
import com.acme.shop.platform.catalog.infrastructure.persistence.jpa.embeddables.MoneyPersistenceEmbeddable;
import com.acme.shop.platform.catalog.infrastructure.persistence.jpa.entities.ProductPersistenceEntity;

public final class ProductPersistenceAssembler {

    private ProductPersistenceAssembler() {
    }

    public static Product toDomainFromPersistence(ProductPersistenceEntity entity) {
        if (entity == null) return null;
        var price = entity.getPrice() == null
                ? null
                : new Money(entity.getPrice().getAmount(), entity.getPrice().getCurrency());
        return new Product(
                new ProductId(entity.getId()),
                entity.getSku(),
                new ProductName(entity.getName()),
                new ProductDescription(entity.getDescription()),
                price,
                new Stock(entity.getStock()),
                new CategoryId(entity.getCategoryId()));
    }

    public static ProductPersistenceEntity toPersistenceFromDomain(Product product) {
        if (product == null) return null;
        var entity = new ProductPersistenceEntity();
        if (product.getId() != null) {
            entity.setId(product.getId().value());
        }
        entity.setSku(product.getSku());
        entity.setName(product.getName().value());
        entity.setDescription(product.getDescription().value());
        entity.setPrice(new MoneyPersistenceEmbeddable(
                product.getPrice().amount(),
                product.getPrice().currency()));
        entity.setStock(product.getStock().value());
        entity.setCategoryId(product.getCategoryId().value());
        return entity;
    }
}
