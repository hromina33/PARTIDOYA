package com.acme.shop.platform.catalog.infrastructure.persistence.jpa.entities;

import com.acme.shop.platform.catalog.domain.model.valueobjects.Sku;
import com.acme.shop.platform.catalog.infrastructure.persistence.jpa.converters.SkuPersistenceConverter;
import com.acme.shop.platform.catalog.infrastructure.persistence.jpa.embeddables.MoneyPersistenceEmbeddable;
import com.acme.shop.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class ProductPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Convert(converter = SkuPersistenceConverter.class)
    @Column(name = "sku", nullable = false, unique = true, length = 32)
    private Sku sku;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 2000)
    private String description;

    @Embedded
    private MoneyPersistenceEmbeddable price;

    @Column(nullable = false)
    private int stock;

    /**
     * Foreign-key column. This {@code Long} field is the SINGLE writable mapping —
     * the {@link Product} aggregate references its {@code Category} by id only.
     */
    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    /**
     * Read-only JPA companion that exists ONLY so Hibernate emits a named
     * foreign-key constraint in the DDL. Never accessed from code: assemblers
     * work with {@link #categoryId} and {@code FetchType.LAZY} prevents any
     * extra select. Without this mapping, Hibernate would NOT generate a FK,
     * and the ERD would have no integrity between {@code products} and
     * {@code categories}.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "category_id",
            nullable = false,
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_products_category"))
    private CategoryPersistenceEntity category;
}
