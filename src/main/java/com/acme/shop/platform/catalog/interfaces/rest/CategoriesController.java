package com.acme.shop.platform.catalog.interfaces.rest;

import com.acme.shop.platform.catalog.application.commandservices.CategoryCommandService;
import com.acme.shop.platform.catalog.application.queryservices.CategoryQueryService;
import com.acme.shop.platform.catalog.domain.model.queries.GetCategoryByIdQuery;
import com.acme.shop.platform.catalog.domain.model.valueobjects.CategoryId;
import com.acme.shop.platform.catalog.interfaces.rest.resources.CategoryResource;
import com.acme.shop.platform.catalog.interfaces.rest.resources.CreateCategoryResource;
import com.acme.shop.platform.catalog.interfaces.rest.transform.CategoryResourceFromEntityAssembler;
import com.acme.shop.platform.catalog.interfaces.rest.transform.CreateCategoryCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/categories", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Categories", description = "Catalog category management endpoints")
public class CategoriesController {
    private final CategoryCommandService categoryCommandService;
    private final CategoryQueryService categoryQueryService;

    public CategoriesController(CategoryCommandService categoryCommandService, CategoryQueryService categoryQueryService) {
        this.categoryCommandService = categoryCommandService;
        this.categoryQueryService = categoryQueryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResource> createCategory(@RequestBody CreateCategoryResource resource) {
        var command = CreateCategoryCommandFromResourceAssembler.toCommandFromResource(resource);
        var category = categoryCommandService.handle(command);
        var resourceResponse = CategoryResourceFromEntityAssembler.toResourceFromEntity(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(resourceResponse);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResource> getCategoryById(@PathVariable Long categoryId) {
        var query = new GetCategoryByIdQuery(new CategoryId(categoryId));
        var category = categoryQueryService.handle(query);
        return category
                .map(CategoryResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
