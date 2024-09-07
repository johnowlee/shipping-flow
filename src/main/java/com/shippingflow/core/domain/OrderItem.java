package com.shippingflow.core.domain;

import lombok.Builder;

public class OrderItem {
    private Item item;
    private int orderQuantity;

    @Builder
    private OrderItem(Item item, int orderQuantity) {
        this.item = item;
        this.orderQuantity = orderQuantity;
    }
}
