package com.shippingflow.infrastructure.db.jpa.item.repository;

import com.shippingflow.core.aggregate.domain.item.dto.ItemAggregateDto;
import com.shippingflow.core.aggregate.domain.item.dto.ItemWithStockDto;
import com.shippingflow.core.aggregate.domain.item.repository.ItemWriterRepository;
import com.shippingflow.infrastructure.db.jpa.item.ItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ItemWriterJpaRepository implements ItemWriterRepository {

    private final ItemJpaRepository itemJpaRepository;

    @Override
    public ItemWithStockDto save(ItemAggregateDto itemAggregateDto) {
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
