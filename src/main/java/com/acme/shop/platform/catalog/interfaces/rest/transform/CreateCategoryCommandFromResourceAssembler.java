package com.acme.shop.platform.catalog.interfaces.rest.transform;

import com.acme.shop.platform.catalog.domain.model.commands.CreateCategoryCommand;
import com.acme.shop.platform.catalog.domain.model.valueobjects.CategoryDescription;
import com.acme.shop.platform.catalog.domain.model.valueobjects.CategoryName;
import com.acme.shop.platform.catalog.interfaces.rest.resources.CreateCategoryResource;

public class CreateCategoryCommandFromResourceAssembler {
    public static CreateCategoryCommand toCommandFromResource(CreateCategoryResource resource) {
        return new CreateCategoryCommand(
                new CategoryName(resource.name()),
                new CategoryDescription(resource.description()));
    }
}
