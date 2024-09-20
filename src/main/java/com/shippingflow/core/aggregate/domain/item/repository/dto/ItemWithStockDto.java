package com.shippingflow.core.aggregate.domain.item.repository.dto;

public record ItemWithStockDto (ItemDto itemDto, StockDto stockDto){
    public static ItemWithStockDto of(ItemDto itemDto, StockDto stockDto) {
        return new ItemWithStockDto(itemDto, stockDto);
    }

    public boolean hasStock() {
        return this.stockDto != null;
    }
}
