package com.shippingflow.core.aggregate.item.repository;

import com.shippingflow.core.aggregate.item.root.Item;

import java.util.Optional;

public interface ItemReaderRepository {
    boolean existsByName(String name);

    Optional<Item> findById(long itemId);
}
