package com.acme.shop.platform.catalog.application.commandservices;

import com.acme.shop.platform.catalog.domain.model.aggregates.Category;
import com.acme.shop.platform.catalog.domain.model.commands.CreateCategoryCommand;

/**
 * Application service contract for write operations over the Category aggregate.
 *
 * <p>Returns the created {@link Category} directly (no {@code Result<T,E>}).
 * Failures are signalled with domain exceptions, which the global handler maps
 * to HTTP responses.</p>
 */
public interface CategoryCommandService {
    Category handle(CreateCategoryCommand command);
}
