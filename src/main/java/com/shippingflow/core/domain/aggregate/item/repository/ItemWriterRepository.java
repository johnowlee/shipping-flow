package com.shippingflow.core.domain.aggregate.item.repository;

import com.shippingflow.core.domain.aggregate.item.dto.ItemAggregateDto;
import com.shippingflow.core.domain.aggregate.item.dto.ItemWithStockDto;

public interface ItemWriterRepository {

    ItemWithStockDto saveNewItem(ItemAggregateDto itemAggregateDto);

    ItemWithStockDto updateStock(ItemAggregateDto itemAggregateDto);
}