package com.acme.shop.platform.catalog.infrastructure.persistence.jpa.entities;

import com.acme.shop.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA persistence entity for categories. Mirrors the {@code Category} aggregate
 * but lives in infrastructure, with all JPA annotations isolated here.
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
public class CategoryPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Column(nullable = false, unique = true, length = 64)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;
}
