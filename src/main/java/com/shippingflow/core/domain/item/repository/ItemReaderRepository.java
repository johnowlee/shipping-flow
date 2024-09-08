package com.shippingflow.core.domain.item.repository;

public interface ItemReaderRepository {
    boolean existsByName(String name);
}
