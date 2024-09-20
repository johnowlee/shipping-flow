package com.shippingflow.core.aggregate.domain.item.dto;

import com.shippingflow.core.aggregate.domain.item.local.StockTransactionType;

import java.time.LocalDateTime;

public record StockTransactionDto(long id, long quantity, StockTransactionType transactionType, LocalDateTime transactionDateTime) {
    public static StockTransactionDto of(long id, long quantity, StockTransactionType transactionType, LocalDateTime transactionDateTime) {
        return new StockTransactionDto(id, quantity, transactionType, transactionDateTime);
    }
}
