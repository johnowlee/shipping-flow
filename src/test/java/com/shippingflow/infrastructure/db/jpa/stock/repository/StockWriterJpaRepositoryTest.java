package com.shippingflow.infrastructure.db.jpa.stock.repository;

import com.shippingflow.core.domain.item.Item;
import com.shippingflow.core.domain.stock.Stock;
import com.shippingflow.infrastructure.db.jpa.item.ItemEntity;
import com.shippingflow.infrastructure.db.jpa.item.repository.ItemJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@Import(StockWriterJpaRepository.class)
@DataJpaTest
class StockWriterJpaRepositoryTest {

    @Autowired
    StockJpaRepository stockJpaRepository;

    @Autowired
    StockWriterJpaRepository stockWriterJpaRepository;

    @Autowired
    ItemJpaRepository itemJpaRepository;

    @DisplayName("재고를 등록한다.")
    @Test
    void save() {
        // given
        String name = "ItemA";
        long price = 1000L;
        String description = "This is ItemA";
        Item item = Item.createNewItem(name, price, description);
        ItemEntity savedItemEntity = itemJpaRepository.save(ItemEntity.createNewFrom(item));
        Item savedItem = savedItemEntity.toDomain();

        Stock stock = Stock.createNewStock(savedItem);

        // when
        Stock actual = stockWriterJpaRepository.save(stock);

        // then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getQuantity()).isZero();
        assertThat(actual.getItem()).isEqualTo(savedItem);
        assertThat(actual.getItem().getPrice()).isEqualTo(price);
        assertThat(actual.getItem().getDescription()).isEqualTo(description);
    }
}