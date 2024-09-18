package com.shippingflow.core.aggregate.vo;

import java.util.List;

public record StockVo(
        Long id,
        Long quantity,
        List<StockTransactionVo>transactions) {}
