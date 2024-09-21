package com.shippingflow.core.aggregate.domain.item.repository;

import com.shippingflow.core.aggregate.domain.item.dto.ItemAggregateDto;
import com.shippingflow.core.aggregate.domain.item.dto.ItemWithStockDto;

public interface ItemWriterRepository {

    ItemWithStockDto save(ItemAggregateDto itemAggregateDto);

    ItemWithStockDto updateStock(ItemAggregateDto itemAggregateDto);
}