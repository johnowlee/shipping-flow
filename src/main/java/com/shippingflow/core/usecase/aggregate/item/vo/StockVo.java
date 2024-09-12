package com.shippingflow.core.usecase.aggregate.item.vo;

import com.shippingflow.core.aggregate.item.root.Item;

import java.util.List;

public record StockVo(
        Long id,
        Item item,
        Long quantity,
        List<StockTransactionVo>transactions) {}
