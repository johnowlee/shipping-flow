package com.shippingflow.core.aggregate.domain.item.repository.dto;

import java.util.List;

public record ItemAggregateDto(ItemDto itemDto, StockDto stockDto, List<StockTransactionDto> stockTransactionDtoList){
    public static ItemAggregateDto of(ItemDto itemDto, StockDto stockDto, List<StockTransactionDto> stockTransactionDtoList) {
        return new ItemAggregateDto(itemDto, stockDto, stockTransactionDtoList);
    }
}
