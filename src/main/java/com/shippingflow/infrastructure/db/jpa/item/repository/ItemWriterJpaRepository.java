package com.shippingflow.infrastructure.db.jpa.item.repository;

import com.shippingflow.core.domain.aggregate.item.repository.ItemWriterRepository;
import com.shippingflow.core.domain.aggregate.item.root.Item;
import com.shippingflow.core.usecase.aggregate.item.vo.ItemVo;
import com.shippingflow.infrastructure.db.jpa.item.ItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ItemWriterJpaRepository implements ItemWriterRepository {

    private final ItemJpaRepository itemJpaRepository;

    @Override
    public Item save(ItemVo itemVo) {
        ItemEntity itemEntity = ItemEntity.createFrom(itemVo);
        return itemJpaRepository.save(itemEntity).toDomain();
    }

    @Override
    public Item update(ItemVo itemVo) {
        ItemEntity itemEntity = ItemEntity.createFrom(itemVo);
        return itemJpaRepository.save(itemEntity).toDomain();
    }
}
