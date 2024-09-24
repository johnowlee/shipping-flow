package com.shippingflow.infrastructure.db.item.jpa.repository;

import com.shippingflow.core.domain.aggregate.item.dto.ItemAggregateDto;
import com.shippingflow.core.domain.aggregate.item.dto.ItemWithStockDto;
import com.shippingflow.core.domain.aggregate.item.repository.ItemWriterRepository;
import com.shippingflow.infrastructure.db.item.jpa.entity.ItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

// TODO: port, adapter 분리?
@RequiredArgsConstructor
@Repository
public class ItemWriterJpaRepository implements ItemWriterRepository {

    private final ItemJpaRepository itemJpaRepository;

    @Override
    public ItemWithStockDto saveNewItem(ItemAggregateDto itemAggregateDto) {
        ItemEntity itemEntity = ItemEntity.createFrom(itemAggregateDto);
        return itemJpaRepository.save(itemEntity).toItemWithStockDto();
    }

    @Override
    public ItemWithStockDto updateStock(ItemAggregateDto itemAggregateDto) {
        ItemEntity itemEntity = ItemEntity.buildFrom(itemAggregateDto);
        return itemJpaRepository.save(itemEntity).toItemWithStockDto();
    }
}
