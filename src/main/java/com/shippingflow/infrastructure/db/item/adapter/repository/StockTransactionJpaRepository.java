package com.shippingflow.infrastructure.db.item.adapter.repository;

import com.shippingflow.infrastructure.db.item.entity.StockTransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockTransactionJpaRepository extends JpaRepository<StockTransactionEntity, Long> {

    @Query("SELECT st FROM StockTransaction st WHERE st.stock.item.id = :itemId")
    Page<StockTransactionEntity> findAllByItemId(@Param("itemId") long itemId, Pageable pageable);
}
