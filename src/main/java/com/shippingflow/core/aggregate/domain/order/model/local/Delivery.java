package com.shippingflow.core.aggregate.domain.order.model.local;

import com.shippingflow.core.aggregate.domain.order.model.root.Order;

import java.time.LocalDateTime;

public class Delivery {
    private Long id;
    private Order order;
    private DeliveryStatus deliveryStatus;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
    private Address address;
}
