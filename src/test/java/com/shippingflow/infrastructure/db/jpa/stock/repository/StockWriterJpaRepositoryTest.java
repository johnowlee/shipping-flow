//package com.shippingflow.infrastructure.db.jpa.stock.repository;
//
//import com.shippingflow.core.aggregate.item.root.Item;
//import com.shippingflow.core.aggregate.item.local.Stock;
//import com.shippingflow.infrastructure.db.jpa.item.ItemEntity;
//import com.shippingflow.infrastructure.db.jpa.item.repository.ItemJpaRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.ActiveProfiles;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@ActiveProfiles("local")
//@Import(StockWriterJpaRepository.class)
//@DataJpaTest
//class StockWriterJpaRepositoryTest {
//
//    @Autowired
//    StockJpaRepository stockJpaRepository;
//
//    @Autowired
//    StockWriterJpaRepository stockWriterJpaRepository;
//
//    @Autowired
//    ItemJpaRepository itemJpaRepository;
//
//    @DisplayName("재고를 등록한다.")
//    @Test
//    void save() {
//        // given
//        String name = "ItemA";
//        long price = 1000L;
//        String description = "This is ItemA";
//        Item item = Item.create(name, price, description);
//        ItemEntity savedItemEntity = itemJpaRepository.save(ItemEntity.create(item));
//        Item savedItem = savedItemEntity.toDomain();
//
//        Stock stock = Stock.create(savedItem);
//
//        // when
//        Stock actual = stockWriterJpaRepository.save(stock);
//
//        // then
//        assertThat(actual.getId()).isNotNull();
//        assertThat(actual.getQuantity()).isZero();
//        assertThat(actual.getItem()).isEqualTo(savedItem);
//        assertThat(actual.getItem().getPrice()).isEqualTo(price);
//        assertThat(actual.getItem().getDescription()).isEqualTo(description);
//    }
//
//    @DisplayName("재고의 수량을 변경한다.")
//    @Test
//    void update() {
//        // given
//        String name = "ItemA";
//        long price = 1000L;
//        String description = "This is ItemA";
//        Item item = Item.create(name, price, description);
//        ItemEntity savedItemEntity = itemJpaRepository.save(ItemEntity.create(item));
//        Item savedItem = savedItemEntity.toDomain();
//
//        Stock stock = Stock.create(savedItem);
//        Stock savedStock = stockWriterJpaRepository.save(stock);
//        savedStock.increase(100L);
//
//        Stock increasedStock = stockWriterJpaRepository.update(savedStock);
//        increasedStock.increase(200L);
//
//        // when
//        Stock actual = stockWriterJpaRepository.update(increasedStock);
//
//        // then
//        assertThat(actual.getId()).isNotNull();
//        assertThat(actual.getQuantity()).isEqualTo(100L + 200L);
//        assertThat(actual.getItem()).isEqualTo(savedItem);
//        assertThat(actual.getItem().getPrice()).isEqualTo(price);
//        assertThat(actual.getItem().getDescription()).isEqualTo(description);
//    }
//}