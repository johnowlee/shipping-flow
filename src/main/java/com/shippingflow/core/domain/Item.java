package com.shippingflow.core.domain;

import lombok.Builder;

import java.util.Objects;

public class Item {
    private Long id;
    private String name;
    private Long quantity;

    @Builder
    private Item(Long id, String name, Long quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Item item = (Item) object;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
