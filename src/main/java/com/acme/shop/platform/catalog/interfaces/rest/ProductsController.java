package com.acme.shop.platform.catalog.interfaces.rest;

import com.acme.shop.platform.catalog.application.commandservices.ProductCommandService;
import com.acme.shop.platform.catalog.application.queryservices.ProductQueryService;
import com.acme.shop.platform.catalog.domain.model.queries.GetProductByIdQuery;
import com.acme.shop.platform.catalog.domain.model.valueobjects.ProductId;
import com.acme.shop.platform.catalog.interfaces.rest.resources.CreateProductResource;
import com.acme.shop.platform.catalog.interfaces.rest.resources.ProductResource;
import com.acme.shop.platform.catalog.interfaces.rest.transform.CreateProductCommandFromResourceAssembler;
import com.acme.shop.platform.catalog.interfaces.rest.transform.ProductResourceFromEntityAssembler;
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
@RequestMapping(value = "/api/v1/products", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Products", description = "Catalog product management endpoints")
public class ProductsController {
    private final ProductCommandService productCommandService;
    private final ProductQueryService productQueryService;

    public ProductsController(ProductCommandService productCommandService, ProductQueryService productQueryService) {
        this.productCommandService = productCommandService;
        this.productQueryService = productQueryService;
    }

    @PostMapping
    public ResponseEntity<ProductResource> createProduct(@RequestBody CreateProductResource resource) {
        var command = CreateProductCommandFromResourceAssembler.toCommandFromResource(resource);
        var product = productCommandService.handle(command);
        var body = ProductResourceFromEntityAssembler.toResourceFromEntity(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResource> getProductById(@PathVariable Long productId) {
        var product = productQueryService.handle(new GetProductByIdQuery(new ProductId(productId)));
        return product
                .map(ProductResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
