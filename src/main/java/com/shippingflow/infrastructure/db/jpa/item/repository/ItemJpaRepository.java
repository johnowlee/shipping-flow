package com.shippingflow.infrastructure.db.jpa.item.repository;

import com.shippingflow.infrastructure.db.jpa.item.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemJpaRepository extends JpaRepository<ItemEntity, Long> {

    boolean existsByName(String name);
}
