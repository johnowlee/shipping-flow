package com.shippingflow.core.domain.stock.repository;

import com.shippingflow.core.domain.stock.Stock;

public interface StockWriterRepository {
    Stock save(Stock stock);
}
