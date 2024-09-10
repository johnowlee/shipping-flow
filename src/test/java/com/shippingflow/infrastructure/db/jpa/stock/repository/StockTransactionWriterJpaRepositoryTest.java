package com.shippingflow.infrastructure.db.jpa.stock.repository;

import com.shippingflow.core.domain.item.Item;
import com.shippingflow.core.domain.stock.Stock;
import com.shippingflow.core.domain.stock.StockTransaction;
import com.shippingflow.core.domain.stock.StockTransactionType;
import com.shippingflow.infrastructure.db.jpa.stock.StockEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@Import(StockTransactionWriterJpaRepository.class)
@DataJpaTest
class StockTransactionWriterJpaRepositoryTest {

    @Autowired
    StockJpaRepository stockJpaRepository;

    @Autowired
    StockTransactionWriterJpaRepository stockTransactionWriterJpaRepository;

    @DisplayName("재고 내역을 저장한다.")
    @Test
    void save() {
        // given
        Item item = Item.builder().build();
        Stock newStock = Stock.builder().build();
        item.assignStock(newStock);

        StockEntity newStockEntity = StockEntity.createNewFrom(newStock);
        StockEntity savedStockEntity = stockJpaRepository.save(newStockEntity);

        Stock savedStock = savedStockEntity.toDomain();
        long quantity = 100L;
        StockTransactionType transactionType = StockTransactionType.INCREASE;
        LocalDateTime transactionDateTIme = LocalDateTime.of(2024, 9, 10, 21, 0, 0);
        StockTransaction newStockTransaction = StockTransaction.createNewStockTransaction(savedStock, quantity, transactionType, transactionDateTIme);

        // when
        StockTransaction actual = stockTransactionWriterJpaRepository.save(newStockTransaction);

        // then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getStock()).isEqualTo(savedStock);
        assertThat(actual.getQuantity()).isEqualTo(quantity);
        assertThat(actual.getTransactionType()).isEqualTo(transactionType);
        assertThat(actual.getTransactionDateTime()).isEqualTo(transactionDateTIme);
    }

}