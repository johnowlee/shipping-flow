package com.shippingflow.core.domain.aggregate.item.repository;

import com.shippingflow.core.domain.aggregate.item.local.Stock;

import java.util.Optional;

public interface StockReaderRepository {
    Optional<Stock> findById(long id);
}
