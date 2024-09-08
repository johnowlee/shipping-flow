package com.shippingflow.core.domain.delivery;

import com.shippingflow.core.domain.order.Order;

import java.time.LocalDateTime;

public class Delivery {
    private Long id;
    private Order order;
    private DeliveryStatus deliveryStatus;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
    private Address address;
}
