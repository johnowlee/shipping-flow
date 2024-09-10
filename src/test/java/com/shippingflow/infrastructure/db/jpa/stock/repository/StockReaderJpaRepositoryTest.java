package com.shippingflow.infrastructure.db.jpa.stock.repository;

import com.shippingflow.core.domain.item.Item;
import com.shippingflow.core.domain.stock.Stock;
import com.shippingflow.infrastructure.db.jpa.item.ItemEntity;
import com.shippingflow.infrastructure.db.jpa.item.repository.ItemJpaRepository;
import com.shippingflow.infrastructure.db.jpa.stock.StockEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@Import(StockReaderJpaRepository.class)
@DataJpaTest
class StockReaderJpaRepositoryTest {

    @Autowired
    StockJpaRepository stockJpaRepository;

    @Autowired
    ItemJpaRepository itemJpaRepository;

    @Autowired
    StockReaderJpaRepository stockReaderJpaRepository;

    @DisplayName("재고 ID로 재고를 조회한다.")
    @Test
    void findById() {
        // given
        Item newItem = Item.createNewItem("itemA", 1000L, "this is itemA");
        ItemEntity newItemEntity = ItemEntity.createNewFrom(newItem);
        ItemEntity savedItemEntity = itemJpaRepository.save(newItemEntity);
        Item savedItem = savedItemEntity.toDomain();

        Stock newStock = Stock.createNewStock(savedItem);
        StockEntity newStockEntity = StockEntity.createNewFrom(newStock);
        StockEntity savedStockEntity = stockJpaRepository.save(newStockEntity);
        Stock savedStock = savedStockEntity.toDomain();

        // when
        Optional<Stock> actual = stockReaderJpaRepository.findById(savedStock.getId());

        // then
        assertThat(actual).isNotEmpty();

        Stock stock = actual.get();
        assertThat(stock).isEqualTo(savedStock);
        assertThat(stock.getItem()).isEqualTo(savedItem);
    }

    @DisplayName("재고 ID로 조회된 엔티티가 없으면 비어있는 Optional을 반환한다.")
    @Test
    void findById_shouldReturnEmptyOptionalWhenStockEntityIsNotFoundById() {
        // when
        Optional<Stock> actual = stockReaderJpaRepository.findById(99L);

        // then
        assertThat(actual).isEmpty();
    }
}