package com.shippingflow.core.usecase.aggregate.item.vo;

import java.util.List;

public record StockVo(
        Long id,
        Long quantity,
        List<StockTransactionVo>transactions) {}
