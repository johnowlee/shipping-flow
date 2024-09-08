package com.shippingflow.core.domain.stock;

import com.shippingflow.core.domain.item.Item;
import lombok.Builder;

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
}
