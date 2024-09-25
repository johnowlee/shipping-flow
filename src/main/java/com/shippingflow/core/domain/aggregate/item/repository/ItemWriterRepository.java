package com.shippingflow.core.domain.aggregate.item.repository;

import com.shippingflow.core.domain.aggregate.item.dto.ItemSaveDto;
import com.shippingflow.core.domain.aggregate.item.dto.ItemWithStockDto;

public interface ItemWriterRepository {

    ItemWithStockDto saveNewItem(ItemSaveDto itemSaveDto);

    ItemWithStockDto updateStock(ItemSaveDto itemSaveDto);
}