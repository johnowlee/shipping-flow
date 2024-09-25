package com.shippingflow.infrastructure.db.item.adapter;

import com.shippingflow.infrastructure.db.item.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemJpaRepository extends JpaRepository<ItemEntity, Long> {

    boolean existsByName(String name);
}
