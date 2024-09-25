package com.shippingflow.infrastructure.db.item.port;

import com.shippingflow.infrastructure.db.item.entity.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ItemReaderPort {

    boolean existsByName(String name);

    Optional<ItemEntity> findItemById(long itemId);

    Page<ItemEntity> findAllItems(Pageable pageable);
}
