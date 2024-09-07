package com.shippingflow.core.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class OrderItem {
    private Item item;
    private int orderQuantity;

    @Builder
    private OrderItem(Item item, int orderQuantity) {
        validateArgs(item, orderQuantity);

        this.item = item;
        this.orderQuantity = orderQuantity;
    }

    private static void validateArgs(Item item, int orderQuantity) {
        if (item == null) {
            throw new IllegalArgumentException("상품은 필수입니다.");
        }
        if (orderQuantity < 1) {
            throw new IllegalArgumentException("상품 주문 갯수는 1개 이상이어야 합니다.");
        }
    }


    public static OrderItem createOrderItem(Item item, int orderQuantity) {
        return builder()
                .item(item)
                .orderQuantity(orderQuantity)
                .build();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        OrderItem orderItem = (OrderItem) object;
        return orderQuantity == orderItem.orderQuantity && Objects.equals(item, orderItem.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, orderQuantity);
    }
}
