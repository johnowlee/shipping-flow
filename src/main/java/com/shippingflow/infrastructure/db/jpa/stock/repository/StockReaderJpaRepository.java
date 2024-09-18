package com.shippingflow.infrastructure.db.jpa.stock.repository;

import com.shippingflow.core.aggregate.domain.item.local.Stock;
import com.shippingflow.core.aggregate.domain.item.repository.StockReaderRepository;
import com.shippingflow.infrastructure.db.jpa.stock.StockEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class StockReaderJpaRepository implements StockReaderRepository {

    private final StockJpaRepository stockJpaRepository;

    @Override
    public Optional<Stock> findById(long id) {
        return stockJpaRepository.findById(id)
                .map(StockEntity::toDomain);
    }
}
