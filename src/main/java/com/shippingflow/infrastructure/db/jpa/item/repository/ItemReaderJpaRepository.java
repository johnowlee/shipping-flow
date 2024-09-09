package com.shippingflow.infrastructure.db.jpa.item.repository;

import com.shippingflow.core.domain.item.repository.ItemReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ItemReaderJpaRepository implements ItemReaderRepository {

    private final ItemJpaRepository itemJpaRepository;

    @Override
    public boolean existsByName(String name) {
        return itemJpaRepository.existsByName(name);
    }
}
