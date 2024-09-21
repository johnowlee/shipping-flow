package com.shippingflow.core.domain.aggregate.order.model.local;

import com.shippingflow.core.domain.aggregate.item.model.root.Item;
import com.shippingflow.core.domain.aggregate.order.model.root.Order;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class OrderItem {
    private Long id;
    private int orderQuantity;
    private Order order;
    private Item item;

    @Builder
    private OrderItem(Long id, Item item, Order order, int orderQuantity) {
        validateArgs(item, orderQuantity);
        this.id = id;
        this.item = item;
        this.order = order;
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
        return Objects.equals(id, orderItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
