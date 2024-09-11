package com.shippingflow.infrastructure.db.jpa.stock.repository;

import com.shippingflow.core.domain.stock.Stock;
import com.shippingflow.core.domain.stock.repository.StockWriterRepository;
import com.shippingflow.infrastructure.db.jpa.stock.StockEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class StockWriterJpaRepository implements StockWriterRepository {

    private final StockJpaRepository stockJpaRepository;

    @Override
    public Stock save(Stock stock) {
        StockEntity stockEntity = StockEntity.createNewFrom(stock);
        return stockJpaRepository.save(stockEntity).toDomain();
    }

    @Override
    public Stock update(Stock stock) {
        StockEntity stockEntity = StockEntity.from(stock);
        return stockJpaRepository.save(stockEntity).toDomain();
    }
}
