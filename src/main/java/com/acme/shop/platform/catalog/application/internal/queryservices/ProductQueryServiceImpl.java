package com.acme.shop.platform.catalog.application.internal.queryservices;

import com.acme.shop.platform.catalog.application.queryservices.ProductQueryService;
import com.acme.shop.platform.catalog.domain.model.aggregates.Product;
import com.acme.shop.platform.catalog.domain.model.queries.GetProductByIdQuery;
import com.acme.shop.platform.catalog.domain.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductQueryServiceImpl implements ProductQueryService {
    private final ProductRepository productRepository;

    public ProductQueryServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Optional<Product> handle(GetProductByIdQuery query) {
        return productRepository.findById(query.productId());
    }
}
