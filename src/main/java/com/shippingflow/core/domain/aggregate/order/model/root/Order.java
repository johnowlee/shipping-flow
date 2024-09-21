package com.shippingflow.core.domain.aggregate.order.model.root;

import com.shippingflow.core.domain.aggregate.customer.model.root.Customer;
import com.shippingflow.core.domain.aggregate.order.model.local.OrderItem;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
public class Order {
    private Long id;
    private Customer customer;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<OrderItem> orderItems;

    @Builder
    private Order(Long id, Customer customer, Set<OrderItem> orderItems, LocalDateTime createdAt, LocalDateTime updatedAt) {
        validateArgs(customer, orderItems);
        this.id = id;
        this.customer = customer;
        this.orderItems = orderItems;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private static Order of(Long id, Customer customer, Set<OrderItem> orderItems, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return builder()
                .id(id)
                .customer(customer)
                .orderItems(orderItems)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    public static Order createOrder(Customer customer, Set<OrderItem> orderItems) {
        return of(null, customer, orderItems, LocalDateTime.now(), null);
    }

    private static void validateArgs(Customer customer, Set<OrderItem> orderItems) {
        if (customer == null) {
            throw new IllegalArgumentException("고객은 필수 입니다.");
        }
        if (orderItems == null) {
            throw new IllegalArgumentException("주문 상품 목록은 필수 입니다.");
        }
        if (orderItems.isEmpty()) {
            throw new IllegalArgumentException("최소 주문 상품은 1개 이상입니다.");
        }
    }
}
