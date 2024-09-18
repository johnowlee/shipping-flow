package com.shippingflow.core.aggregate.vo;

public record ItemVo(
        Long id,
        String name,
        Long price,
        String description,
        StockVo stock) {}
