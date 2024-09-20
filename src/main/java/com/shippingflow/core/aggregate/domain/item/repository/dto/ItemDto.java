package com.shippingflow.core.aggregate.domain.item.repository.dto;

public record ItemDto(long id, String name, Long price, String description) {
    public static ItemDto of(long id, String name, Long price, String description) {
        return new ItemDto(id, name, price, description);
    }
}
