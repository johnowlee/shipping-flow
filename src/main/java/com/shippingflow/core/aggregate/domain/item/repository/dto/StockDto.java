package com.shippingflow.core.aggregate.domain.item.repository.dto;

public record StockDto(long id, long quantity) {
    public static StockDto of(long id, long quantity) {
        return new StockDto(id, quantity);
    }
}
