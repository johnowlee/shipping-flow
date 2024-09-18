package com.shippingflow.core.aggregate.domain.item.repository;

import com.shippingflow.core.aggregate.domain.item.local.Stock;

import java.util.Optional;

public interface StockReaderRepository {
    Optional<Stock> findById(long id);
}
