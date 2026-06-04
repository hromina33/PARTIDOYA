package com.acme.shop.platform.catalog.application.internal.queryservices;

import com.acme.shop.platform.catalog.application.queryservices.CategoryQueryService;
import com.acme.shop.platform.catalog.domain.model.aggregates.Category;
import com.acme.shop.platform.catalog.domain.model.queries.GetCategoryByIdQuery;
import com.acme.shop.platform.catalog.domain.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryQueryServiceImpl implements CategoryQueryService {
    private final CategoryRepository categoryRepository;

    public CategoryQueryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Optional<Category> handle(GetCategoryByIdQuery query) {
        return categoryRepository.findById(query.categoryId());
    }
}
