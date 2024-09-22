package com.shippingflow.core.domain.aggregate.item.dto;

public record ItemDto(Long id, String name, Long price, String description) {
    public static ItemDto of(Long id, String name, Long price, String description) {
        return new ItemDto(id, name, price, description);
    }
}
