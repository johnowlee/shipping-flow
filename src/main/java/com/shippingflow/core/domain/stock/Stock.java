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
    private Stock(Long id, long quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public void assignedTo(Item item) {
        this.item = item;
    }

    public static Stock createNewStock(Item item) {
        Stock stock = Stock.builder().build();
        stock.assignedTo(item);
        return stock;
    }

    public static Stock createStock(Long id, Item item, long quantity) {
        Stock stock = Stock.builder().id(id).quantity(quantity).build();
        stock.assignedTo(item);
        return stock;
    }
}
