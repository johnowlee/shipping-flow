package com.shippingflow.core.aggregate.domain.item.repository;

import com.shippingflow.core.aggregate.domain.item.repository.dto.ItemWithStockDto;

import java.util.Optional;

public interface ItemReaderRepository {
    boolean existsByName(String name);

    Optional<ItemWithStockDto> findItemWithStockById(long itemId);
}
