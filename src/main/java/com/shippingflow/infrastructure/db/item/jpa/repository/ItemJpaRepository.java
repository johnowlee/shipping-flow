package com.shippingflow.infrastructure.db.item.jpa.repository;

import com.shippingflow.infrastructure.db.item.jpa.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemJpaRepository extends JpaRepository<ItemEntity, Long> {

    boolean existsByName(String name);
}
