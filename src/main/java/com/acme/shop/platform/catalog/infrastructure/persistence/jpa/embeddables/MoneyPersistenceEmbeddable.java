package com.acme.shop.platform.catalog.infrastructure.persistence.jpa.embeddables;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class MoneyPersistenceEmbeddable {

    @Column(name = "price_amount", precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(name = "price_currency", length = 3)
    private String currency;

    public MoneyPersistenceEmbeddable() {
    }

    public MoneyPersistenceEmbeddable(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}
