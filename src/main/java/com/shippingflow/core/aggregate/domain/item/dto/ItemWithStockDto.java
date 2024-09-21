package com.shippingflow.core.aggregate.domain.item.dto;

public record ItemWithStockDto (ItemDto item, StockDto stock){
    public static ItemWithStockDto of(ItemDto item, StockDto stock) {
        return new ItemWithStockDto(item, stock);
    }

    public boolean hasStock() {
        return this.stock != null;
    }
}
