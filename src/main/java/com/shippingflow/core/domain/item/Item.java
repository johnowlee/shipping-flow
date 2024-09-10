package com.shippingflow.core.domain.item;

import com.shippingflow.core.domain.stock.Stock;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Item {
    private Long id;
    private String name;
    private Long price;
    private String description;
    private Stock stock;

    @Builder
    private Item(Long id, String name, Long price, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public void assignStock(Stock stock) {
        if (this.stock != null) return;
        this.stock = stock;
        stock.assignedTo(this);
    }

    public static Item createNewItem(String name, Long price, String description) {
        return of(null, name, price, description);
    }

    public static Item createItem(Long id, String name, Long price, String description) {
        return of(id, name, price, description);
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

    private static Item of(Long id, String name, Long price, String description) {
        return builder()
                .id(id)
                .name(name)
                .price(price)
                .description(description)
                .build();
    }
}
