package com.shippingflow.core.domain.aggregate.item.repository;

import com.shippingflow.core.domain.aggregate.item.dto.ItemWithStockDto;

import java.util.Optional;

public interface ItemReaderRepository {
    boolean existsByName(String name);

    Optional<ItemWithStockDto> findItemWithStockById(long itemId);
}
