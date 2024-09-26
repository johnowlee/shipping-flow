package com.shippingflow.infrastructure.db.item.adapter;

import com.shippingflow.infrastructure.db.item.adapter.repository.ItemJpaRepository;
import com.shippingflow.infrastructure.db.item.entity.ItemEntity;
import com.shippingflow.infrastructure.db.item.port.ItemWriterPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ItemWriterAdapter implements ItemWriterPort {

    private final ItemJpaRepository itemJpaRepository;

    @Override
    public ItemEntity save(ItemEntity itemEntity) {
        return itemJpaRepository.save(itemEntity);
    }
}
