package com.acme.shop.platform.catalog.application.internal.commandservices;

import com.acme.shop.platform.catalog.application.commandservices.ProductCommandService;
import com.acme.shop.platform.catalog.domain.model.aggregates.Product;
import com.acme.shop.platform.catalog.domain.model.commands.CreateProductCommand;
import com.acme.shop.platform.catalog.domain.repositories.CategoryRepository;
import com.acme.shop.platform.catalog.domain.repositories.ProductRepository;
import com.acme.shop.platform.shared.domain.exceptions.ResourceConflictException;
import com.acme.shop.platform.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ProductCommandServiceImpl implements ProductCommandService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductCommandServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product handle(CreateProductCommand command) {
        if (productRepository.existsBySku(command.sku())) {
            throw new ResourceConflictException(
                    "Product",
                    "Sku '%s' already exists".formatted(command.sku().code()));
        }
        if (!categoryRepository.existsById(command.categoryId())) {
            throw new ResourceNotFoundException("Category", command.categoryId().value().toString());
        }
        var product = new Product(command);
        return productRepository.save(product);
    }
}
