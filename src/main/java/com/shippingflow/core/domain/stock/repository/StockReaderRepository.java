package com.shippingflow.core.domain.stock.repository;

import com.shippingflow.core.domain.stock.Stock;

import java.util.Optional;

public interface StockReaderRepository {
    Optional<Stock> findById(long id);
}
