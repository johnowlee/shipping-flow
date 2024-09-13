package com.shippingflow.core.usecase.aggregate.item.vo;

public record ItemVo(
        Long id,
        String name,
        Long price,
        String description,
        StockVo stock) {}
