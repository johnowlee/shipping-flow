package com.shippingflow.core.aggregate.domain.item.repository;

import com.shippingflow.core.aggregate.domain.item.repository.dto.ItemWithStockDto;
import com.shippingflow.core.aggregate.domain.item.root.Item;

import java.util.Optional;

public interface ItemReaderRepository {
    boolean existsByName(String name);

    Optional<Item> findById(long itemId);

    Optional<ItemWithStockDto> findItemWithStockById(long itemId);
}
