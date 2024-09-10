package com.shippingflow.infrastructure.db.jpa.stock.repository;

import com.shippingflow.infrastructure.db.jpa.stock.StockTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockTransactionJpaRepository extends JpaRepository<StockTransactionEntity, Long> {
}
