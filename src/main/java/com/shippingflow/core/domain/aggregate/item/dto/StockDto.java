package com.shippingflow.core.domain.aggregate.item.dto;

public record StockDto(Long id, long quantity) {
    public static StockDto of(Long id, long quantity) {
        return new StockDto(id, quantity);
    }
}
