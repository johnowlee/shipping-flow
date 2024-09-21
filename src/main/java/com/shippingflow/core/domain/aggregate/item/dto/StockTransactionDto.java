package com.shippingflow.core.domain.aggregate.item.dto;

import com.shippingflow.core.domain.aggregate.item.model.local.StockTransactionType;

import java.time.LocalDateTime;

public record StockTransactionDto(long id, long quantity, StockTransactionType transactionType, LocalDateTime transactionDateTime) {
    public static StockTransactionDto of(long id, long quantity, StockTransactionType transactionType, LocalDateTime transactionDateTime) {
        return new StockTransactionDto(id, quantity, transactionType, transactionDateTime);
    }
}
