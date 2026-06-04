package com.acme.shop.platform.catalog.infrastructure.persistence.jpa.converters;

import com.acme.shop.platform.catalog.domain.model.valueobjects.Sku;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class SkuPersistenceConverter implements AttributeConverter<Sku, String> {

    @Override
    public String convertToDatabaseColumn(Sku attribute) {
        return attribute == null ? null : attribute.code();
    }

    @Override
    public Sku convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new Sku(dbData);
    }
}
