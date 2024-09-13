package com.shippingflow.core.domain.aggregate.item.repository;

import com.shippingflow.core.domain.aggregate.item.root.Item;

import java.util.Optional;

public interface ItemReaderRepository {
    boolean existsByName(String name);

    Optional<Item> findById(long itemId);
}
