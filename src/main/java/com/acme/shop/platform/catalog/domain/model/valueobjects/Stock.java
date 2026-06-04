package com.acme.shop.platform.catalog.domain.model.valueobjects;

public record Stock(int value) {
    public Stock {
        if (value < 0) {
            throw new IllegalArgumentException("stock must not be negative");
        }
    }
}
