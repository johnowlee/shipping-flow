package com.shippingflow.core.aggregate.domain.order.model.local;

public enum DeliveryStatus {
    READY("배송준비"), SHIPPED("발송완료"), DELIVERED("배송완료"),
    ;

    private final String description;

    DeliveryStatus(String description) {
        this.description = description;
    }
}
