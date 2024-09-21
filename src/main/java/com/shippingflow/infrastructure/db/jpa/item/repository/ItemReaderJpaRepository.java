package com.shippingflow.infrastructure.db.jpa.item.repository;

import com.shippingflow.core.aggregate.domain.item.repository.ItemReaderRepository;
import com.shippingflow.core.aggregate.domain.item.dto.ItemWithStockDto;
import com.shippingflow.infrastructure.db.jpa.item.ItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ItemReaderJpaRepository implements ItemReaderRepository {

    private final ItemJpaRepository itemJpaRepository;

    @Override
    public boolean existsByName(String name) {
        return itemJpaRepository.existsByName(name);
    }

    @Override
    public Optional<ItemWithStockDto> findItemWithStockById(long itemId) {
       return itemJpaRepository.findById(itemId)
               .map(ItemEntity::toItemWithStockDto);
    }
}
