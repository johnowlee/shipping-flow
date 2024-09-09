package com.shippingflow.core.domain.stock;

import com.shippingflow.core.domain.item.Item;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Stock {
    private Long id;

    private Item item;

    private long quantity;

    @Builder
    private Stock(Long id, Item item, long quantity) {
        this.id = id;
        this.item = item;
        this.quantity = quantity;
    }

    public static Stock createNewStock(Item item) {
        return Stock.builder().item(item).build();
    }

    public static Stock createStock(Long id, Item item, long quantity) {
        return Stock.builder().id(id).item(item).quantity(quantity).build();
    }
}
