package com.shippingflow.infrastructure.db.jpa.item.repository;

import com.shippingflow.core.domain.aggregate.item.dto.ItemAggregateDto;
import com.shippingflow.core.domain.aggregate.item.dto.ItemWithStockDto;
import com.shippingflow.core.domain.aggregate.item.repository.ItemWriterRepository;
import com.shippingflow.infrastructure.db.jpa.item.ItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ItemWriterJpaRepository implements ItemWriterRepository {

    private final ItemJpaRepository itemJpaRepository;

    @Override
    public ItemWithStockDto saveNewItem(ItemAggregateDto itemAggregateDto) {
//        ItemEntity itemEntity = ItemEntity.buildFrom(itemVo);
//        return itemJpaRepository.save(itemEntity).toDomain();
        return null;
    }

    @Override
    public ItemWithStockDto updateStock(ItemAggregateDto itemAggregateDto) {
        ItemEntity itemEntity = ItemEntity.buildFrom(itemAggregateDto);
        return itemJpaRepository.save(itemEntity).toItemWithStockDto();
    }
}
