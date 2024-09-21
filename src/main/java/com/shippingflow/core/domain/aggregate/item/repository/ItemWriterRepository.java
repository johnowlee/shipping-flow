package com.shippingflow.core.domain.aggregate.item.repository;

import com.shippingflow.core.domain.aggregate.item.dto.ItemAggregateDto;
import com.shippingflow.core.domain.aggregate.item.dto.ItemWithStockDto;

public interface ItemWriterRepository {

    ItemWithStockDto save(ItemAggregateDto itemAggregateDto);

    ItemWithStockDto updateStock(ItemAggregateDto itemAggregateDto);
}