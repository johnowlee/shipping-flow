package com.shippingflow.infrastructure.db.jpa.stock.repository;

import com.shippingflow.infrastructure.db.jpa.stock.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockJpaRepository extends JpaRepository<StockEntity, Long> {
}
