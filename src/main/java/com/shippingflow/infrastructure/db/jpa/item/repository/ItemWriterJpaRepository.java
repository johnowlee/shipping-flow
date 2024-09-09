package com.shippingflow.infrastructure.db.jpa.item.repository;

import com.shippingflow.core.domain.item.Item;
import com.shippingflow.core.domain.item.repository.ItemWriterRepository;
import com.shippingflow.infrastructure.db.jpa.item.ItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ItemWriterJpaRepository implements ItemWriterRepository {

    private final ItemJpaRepository itemJpaRepository;

    @Override
    public Item save(Item item) {
        ItemEntity itemEntity = ItemEntity.createNewFrom(item);
        return itemJpaRepository.save(itemEntity).toDomain();
    }
}
