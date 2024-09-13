package com.shippingflow.core.aggregate.item.repository;

import com.shippingflow.core.aggregate.item.local.Stock;

import java.util.Optional;

public interface StockReaderRepository {
    Optional<Stock> findById(long id);
}
