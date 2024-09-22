package com.shippingflow.core.domain.aggregate.item.dto;

import java.util.List;

public record ItemAggregateDto(ItemDto item, StockDto stock, List<StockTransactionDto> transactions){
    public static ItemAggregateDto of(ItemDto item, StockDto stock, List<StockTransactionDto> transactions) {
        return new ItemAggregateDto(item, stock, transactions);
    }

    public boolean isStockAbsent() {
        return stock == null;
    }
}
