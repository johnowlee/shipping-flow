package com.shippingflow.core.usecase.aggregate.item.vo;

import com.shippingflow.core.domain.aggregate.item.local.StockTransactionType;

import java.time.LocalDateTime;

public record StockTransactionVo(
        Long id,
        long quantity,
        StockTransactionType transactionType,
        LocalDateTime transactionDateTime) {}
