package com.acme.shop.platform.catalog.interfaces.rest.transform;

import com.acme.shop.platform.catalog.domain.model.commands.CreateProductCommand;
import com.acme.shop.platform.catalog.domain.model.valueobjects.CategoryId;
import com.acme.shop.platform.catalog.domain.model.valueobjects.Money;
import com.acme.shop.platform.catalog.domain.model.valueobjects.ProductDescription;
import com.acme.shop.platform.catalog.domain.model.valueobjects.ProductName;
import com.acme.shop.platform.catalog.domain.model.valueobjects.Sku;
import com.acme.shop.platform.catalog.domain.model.valueobjects.Stock;
import com.acme.shop.platform.catalog.interfaces.rest.resources.CreateProductResource;

public class CreateProductCommandFromResourceAssembler {
    public static CreateProductCommand toCommandFromResource(CreateProductResource resource) {
        return new CreateProductCommand(
                new Sku(resource.sku()),
                new ProductName(resource.name()),
                new ProductDescription(resource.description()),
                new Money(resource.price(), resource.currency()),
                new Stock(resource.stock()),
                new CategoryId(resource.categoryId()));
    }
}
