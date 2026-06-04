package com.acme.shop.platform.catalog.application.internal.commandservices;

import com.acme.shop.platform.catalog.application.commandservices.CategoryCommandService;
import com.acme.shop.platform.catalog.domain.model.aggregates.Category;
import com.acme.shop.platform.catalog.domain.model.commands.CreateCategoryCommand;
import com.acme.shop.platform.catalog.domain.repositories.CategoryRepository;
import com.acme.shop.platform.shared.domain.exceptions.ResourceConflictException;
import org.springframework.stereotype.Service;

@Service
public class CategoryCommandServiceImpl implements CategoryCommandService {
    private final CategoryRepository categoryRepository;

    public CategoryCommandServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category handle(CreateCategoryCommand command) {
        if (categoryRepository.existsByName(command.name())) {
            throw new ResourceConflictException(
                    "Category",
                    "Name '%s' already exists".formatted(command.name().value()));
        }
        var category = new Category(command);
        return categoryRepository.save(category);
    }
}
