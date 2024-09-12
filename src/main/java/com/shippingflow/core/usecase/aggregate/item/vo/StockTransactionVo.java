package com.shippingflow.core.usecase.aggregate.item.vo;

import com.shippingflow.core.aggregate.item.local.StockTransactionType;

import java.time.LocalDateTime;

public record StockTransactionVo(
        Long id,
        StockVo stock, long quantity,
        StockTransactionType transactionType,
        LocalDateTime transactionDateTime) {}
