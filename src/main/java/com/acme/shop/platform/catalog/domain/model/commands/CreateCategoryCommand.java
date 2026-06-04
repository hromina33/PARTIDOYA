package com.acme.shop.platform.catalog.domain.model.commands;

import com.acme.shop.platform.catalog.domain.model.valueobjects.CategoryDescription;
import com.acme.shop.platform.catalog.domain.model.valueobjects.CategoryName;

import java.util.Objects;

public record CreateCategoryCommand(
        CategoryName name,
        CategoryDescription description) {
    public CreateCategoryCommand {
        Objects.requireNonNull(name, "name cannot be null");
        Objects.requireNonNull(description, "description cannot be null");
    }
}
