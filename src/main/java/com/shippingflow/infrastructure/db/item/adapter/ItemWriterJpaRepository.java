package com.shippingflow.infrastructure.db.item.adapter;

import com.shippingflow.core.domain.aggregate.item.dto.ItemAggregateDto;
import com.shippingflow.core.domain.aggregate.item.dto.ItemWithStockDto;
import com.shippingflow.infrastructure.db.item.entity.ItemEntity;
import lombok.RequiredArgsConstructor;

// TODO: port, adapter 분리?
@RequiredArgsConstructor
public class ItemWriterJpaRepository {

    private final ItemJpaRepository itemJpaRepository;

    public ItemWithStockDto saveNewItem(ItemAggregateDto itemAggregateDto) {
        ItemEntity itemEntity = ItemEntity.createFrom(itemAggregateDto);
        return itemJpaRepository.save(itemEntity).toItemWithStockDto();
    }

    public ItemWithStockDto updateStock(ItemAggregateDto itemAggregateDto) {
        ItemEntity itemEntity = ItemEntity.buildFrom(itemAggregateDto);
        return itemJpaRepository.save(itemEntity).toItemWithStockDto();
    }
}
