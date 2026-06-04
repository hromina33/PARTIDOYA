package com.acme.shop.platform.catalog.domain.model.aggregates;

import com.acme.shop.platform.catalog.domain.model.commands.CreateCategoryCommand;
import com.acme.shop.platform.catalog.domain.model.valueobjects.CategoryDescription;
import com.acme.shop.platform.catalog.domain.model.valueobjects.CategoryId;
import com.acme.shop.platform.catalog.domain.model.valueobjects.CategoryName;
import lombok.Getter;

import java.util.Objects;

/**
 * Category aggregate root. Pure domain — no JPA or Spring annotations.
 *
 * <p>All fields are value objects. Identity is immutable from the outside:
 * no setter on {@code id} — it can only be assigned by one of the constructors.</p>
 */
@Getter
public class Category {
    private CategoryId id;
    private CategoryName name;
    private CategoryDescription description;

    protected Category() {
    }

    /**
     * Creation constructor. The id stays {@code null} until JPA assigns one.
     */
    public Category(CategoryName name, CategoryDescription description) {
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.description = Objects.requireNonNull(description, "description must not be null");
    }

    public Category(CreateCategoryCommand command) {
        this(command.name(), command.description());
    }

    /**
     * Rehydration constructor — only invoked by the infrastructure persistence
     * assembler to reconstruct an existing aggregate loaded from the database.
     */
    public Category(CategoryId id, CategoryName name, CategoryDescription description) {
        this(name, description);
        this.id = Objects.requireNonNull(id, "id must not be null");
    }
}
