package com.shippingflow.core.domain.stock.repository;

import com.shippingflow.core.domain.stock.StockTransaction;

public interface StockTransactionWriterRepository {
    StockTransaction save(StockTransaction stockTransaction);
}
