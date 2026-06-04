package com.acme.shop.platform.catalog.domain.model.commands;

import com.acme.shop.platform.catalog.domain.model.valueobjects.CategoryId;
import com.acme.shop.platform.catalog.domain.model.valueobjects.Money;
import com.acme.shop.platform.catalog.domain.model.valueobjects.ProductDescription;
import com.acme.shop.platform.catalog.domain.model.valueobjects.ProductName;
import com.acme.shop.platform.catalog.domain.model.valueobjects.Sku;
import com.acme.shop.platform.catalog.domain.model.valueobjects.Stock;

import java.util.Objects;

public record CreateProductCommand(
        Sku sku,
        ProductName name,
        ProductDescription description,
        Money price,
        Stock stock,
        CategoryId categoryId) {

    public CreateProductCommand {
        Objects.requireNonNull(sku, "sku cannot be null");
        Objects.requireNonNull(name, "name cannot be null");
        Objects.requireNonNull(description, "description cannot be null");
        Objects.requireNonNull(price, "price cannot be null");
        Objects.requireNonNull(stock, "stock cannot be null");
        Objects.requireNonNull(categoryId, "categoryId cannot be null");
    }
}
