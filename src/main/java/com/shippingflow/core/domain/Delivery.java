package com.shippingflow.core.domain;

import java.time.LocalDateTime;

public class Delivery {
    private Long id;
    private Order order;
    private LocalDateTime shippedAt;
}
