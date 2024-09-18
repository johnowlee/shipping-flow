package com.shippingflow.core.aggregate.domain.order.local;

import com.shippingflow.core.aggregate.domain.order.root.Order;

import java.time.LocalDateTime;

public class Delivery {
    private Long id;
    private Order order;
    private DeliveryStatus deliveryStatus;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
    private Address address;
}
