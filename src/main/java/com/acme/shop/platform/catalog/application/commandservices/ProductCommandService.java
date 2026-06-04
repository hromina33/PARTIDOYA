package com.acme.shop.platform.catalog.application.commandservices;

import com.acme.shop.platform.catalog.domain.model.aggregates.Product;
import com.acme.shop.platform.catalog.domain.model.commands.CreateProductCommand;

public interface ProductCommandService {
    Product handle(CreateProductCommand command);
}
