package com.shippingflow.presenter.api.item.controller.response;

import com.shippingflow.core.domain.aggregate.item.dto.StockTransactionDto;
import com.shippingflow.core.domain.aggregate.item.model.local.StockTransactionType;

import java.time.LocalDateTime;

public record StockTransactionResponse(long id, long quantity, StockTransactionType transactionType, LocalDateTime transactionDateTime) {
    public static StockTransactionResponse from(StockTransactionDto dto) {
        return new StockTransactionResponse(dto.id(), dto.quantity(), dto.transactionType(), dto.transactionDateTime());
    }
}
