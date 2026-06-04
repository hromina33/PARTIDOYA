package com.acme.shop.platform.catalog.domain.model.aggregates;

import com.acme.shop.platform.catalog.domain.model.commands.CreateProductCommand;
import com.acme.shop.platform.catalog.domain.model.valueobjects.CategoryId;
import com.acme.shop.platform.catalog.domain.model.valueobjects.Money;
import com.acme.shop.platform.catalog.domain.model.valueobjects.ProductDescription;
import com.acme.shop.platform.catalog.domain.model.valueobjects.ProductId;
import com.acme.shop.platform.catalog.domain.model.valueobjects.ProductName;
import com.acme.shop.platform.catalog.domain.model.valueobjects.Sku;
import com.acme.shop.platform.catalog.domain.model.valueobjects.Stock;
import lombok.Getter;

import java.util.Objects;

/**
 * Product aggregate root. Pure domain — no JPA or Spring annotations.
 *
 * <p>References its {@link Category} by {@link CategoryId} (never by direct
 * association). Identity is immutable from outside.</p>
 */
@Getter
public class Product {
    private ProductId id;
    private Sku sku;
    private ProductName name;
    private ProductDescription description;
    private Money price;
    private Stock stock;

    private CategoryId categoryId;

    protected Product() {
    }

    public Product(Sku sku, ProductName name, ProductDescription description, Money price, Stock stock, CategoryId categoryId) {
        this.sku = Objects.requireNonNull(sku, "sku must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.description = Objects.requireNonNull(description, "description must not be null");
        this.price = Objects.requireNonNull(price, "price must not be null");
        this.stock = Objects.requireNonNull(stock, "stock must not be null");
        this.categoryId = Objects.requireNonNull(categoryId, "categoryId must not be null");
    }

    public Product(CreateProductCommand command) {
        this(
                command.sku(),
                command.name(),
                command.description(),
                command.price(),
                command.stock(),
                command.categoryId());
    }

    /**
     * Rehydration constructor — for the infrastructure persistence assembler only.
     */
    public Product(ProductId id, Sku sku, ProductName name, ProductDescription description, Money price, Stock stock, CategoryId categoryId) {
        this(sku, name, description, price, stock, categoryId);
        this.id = Objects.requireNonNull(id, "id must not be null");
    }
}
