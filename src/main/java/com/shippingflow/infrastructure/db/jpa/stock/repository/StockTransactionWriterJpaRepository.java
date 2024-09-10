package com.shippingflow.infrastructure.db.jpa.stock.repository;

import com.shippingflow.core.domain.stock.StockTransaction;
import com.shippingflow.core.domain.stock.repository.StockTransactionWriterRepository;
import com.shippingflow.infrastructure.db.jpa.stock.StockTransactionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class StockTransactionWriterJpaRepository implements StockTransactionWriterRepository {

    private final StockTransactionJpaRepository stockTransactionJpaRepository;

    @Override
    public StockTransaction save(StockTransaction stockTransaction) {
        StockTransactionEntity newStockTransactionEntity = StockTransactionEntity.createNewFrom(stockTransaction);
        return stockTransactionJpaRepository.save(newStockTransactionEntity).toDomain();
    }
}
