package com.shippingflow.core.domain.aggregate.item.dto;

public record ItemSaveDto(ItemDto item, StockDto stock, StockTransactionDto transaction){
    public static ItemSaveDto of(ItemDto item, StockDto stock, StockTransactionDto transaction) {
        return new ItemSaveDto(item, stock, transaction);
    }

    public boolean isStockAbsent() {
        return stock == null;
    }
}
