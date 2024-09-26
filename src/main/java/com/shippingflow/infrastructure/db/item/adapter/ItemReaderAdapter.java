package com.shippingflow.infrastructure.db.item.adapter;

import com.shippingflow.infrastructure.db.item.adapter.repository.ItemJpaRepository;
import com.shippingflow.infrastructure.db.item.adapter.repository.StockTransactionJpaRepository;
import com.shippingflow.infrastructure.db.item.entity.ItemEntity;
import com.shippingflow.infrastructure.db.item.entity.StockTransactionEntity;
import com.shippingflow.infrastructure.db.item.port.ItemReaderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ItemReaderAdapter implements ItemReaderPort {

    private final ItemJpaRepository itemJpaRepository;
    private final StockTransactionJpaRepository stockTransactionJpaRepository;

    @Override
    public boolean existsByName(String name) {
        return itemJpaRepository.existsByName(name);
    }

    @Override
    public Optional<ItemEntity> findItemById(long itemId) {
        return itemJpaRepository.findById(itemId);
    }

    @Override
    public Page<ItemEntity> findAllItems(Pageable pageable) {
        return itemJpaRepository.findAll(pageable);
    }

    @Override
    public Page<StockTransactionEntity> findAllStockTransactionsByItemId(long itemId, Pageable pageable) {
        return stockTransactionJpaRepository.findAllByItemId(itemId, pageable);
    }
}
