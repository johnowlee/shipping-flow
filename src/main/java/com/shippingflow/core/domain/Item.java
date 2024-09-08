package com.shippingflow.core.domain;

import lombok.Builder;

import java.util.Objects;

public class Item {
    private Long id;
    private String name;
    private Long quantity;
    private String description;

    @Builder
    private Item(Long id, String name, Long quantity, String description) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.description = description;
    }

    public static Item createNewItem(String name, String description) {
        return builder()
                .name(name)
                .description(description)
                .build();
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
