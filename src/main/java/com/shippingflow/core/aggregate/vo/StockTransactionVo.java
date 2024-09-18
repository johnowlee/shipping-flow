package com.shippingflow.core.aggregate.vo;

import com.shippingflow.core.aggregate.domain.item.local.StockTransactionType;

import java.time.LocalDateTime;

public record StockTransactionVo(
        Long id,
        long quantity,
        StockTransactionType transactionType,
        LocalDateTime transactionDateTime) {}
