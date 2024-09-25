package com.shippingflow.infrastructure.db.item.entity;

import com.shippingflow.core.domain.aggregate.item.dto.*;
import com.shippingflow.core.domain.aggregate.item.model.local.StockTransactionType;
import com.shippingflow.infrastructure.db.item.entity.ItemEntity;
import com.shippingflow.infrastructure.db.item.entity.StockEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class ItemEntityTest {
    @DisplayName("ItemEntity에 StockEntity 연관관계를 설정한다.")
    @Test
    void bind() {
        // given
        ItemEntity itemEntity = buildItemEntityForTest();
        StockEntity stockEntity = buildStockEntityForTest();

        // when
        itemEntity.bind(stockEntity);

        // then
        assertThat(itemEntity.getStock()).isEqualTo(stockEntity);
        assertThat(stockEntity.getItem()).isEqualTo(itemEntity);
    }

    @DisplayName("ItemAggregateDto로 부터 StockEntity가 없는 새로운 ItemEntity 객체를 생성한다.")
    @Test
    void createItemEntityWithoutStockEntityFrom_itemAggregateDto() {
        // given
        String name = "newItemA";
        long price = 1000L;
        String description = "this is newItemA";
        ItemDto itemDto = ItemDto.of(null, name, price, description);
        ItemAggregateDto itemAggregateDto = ItemAggregateDto.of(itemDto, null, null);

        // when
        ItemEntity actual = ItemEntity.createFrom(itemAggregateDto);

        // then
        assertThat(actual.getId()).isNull();
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getPrice()).isEqualTo(price);
        assertThat(actual.getDescription()).isEqualTo(description);
        assertThat(actual.getStock()).isNull();
    }

    @DisplayName("ItemAggregateDto로 부터 StockEntity가 있는 새로운 ItemEntity 객체를 생성한다.")
    @Test
    void createItemEntityWithStockEntityFrom_itemAggregateDto() {
        // given
        String name = "newItemA";
        long price = 1000L;
        String description = "this is newItemA";
        ItemDto itemDto = ItemDto.of(null, name, price, description);

        long quantity = 5000L;
        StockDto stockDto = StockDto.of(null, quantity);

        StockTransactionType transactionType = StockTransactionType.INCREASE;
        LocalDateTime transactionDateTime = LocalDateTime.now();
        StockTransactionDto stockTransactionDto = StockTransactionDto.of(null, quantity, transactionType, transactionDateTime);

        ItemAggregateDto itemAggregateDto = ItemAggregateDto.of(itemDto, stockDto, List.of(stockTransactionDto));

        // when
        ItemEntity actual = ItemEntity.createFrom(itemAggregateDto);

        // then
        assertThat(actual.getId()).isNull();
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getPrice()).isEqualTo(price);
        assertThat(actual.getDescription()).isEqualTo(description);
        assertThat(actual.getStock().getId()).isNull();
        assertThat(actual.getStock().getQuantity()).isEqualTo(quantity);
        assertThat(actual.getStock().getTransactions()).hasSize(1)
                .extracting("id", "quantity", "transactionType", "transactionDateTime")
                .contains(
                        tuple(null, quantity, transactionType, transactionDateTime)
                );
    }

    @DisplayName("ItemAggregateDto로 부터 재고 입출고를 위한 ItemEntity 객체를 생성한다.")
    @Test
    void buildItemEntityWithStockAndNewTransactionEntityFrom_itemAggregateDto() {
        // given
        String name = "itemA";
        long price = 1000L;
        String description = "this is itemA";
        ItemDto itemDto = ItemDto.of(10L, name, price, description);

        long quantity = 15_000L;
        StockDto stockDto = StockDto.of(10L, quantity);

        long transactionQuantity = 10_000L;
        StockTransactionType transactionType = StockTransactionType.INCREASE;
        LocalDateTime transactionDateTime = LocalDateTime.now();
        StockTransactionDto stockTransactionDto = StockTransactionDto.of(null, transactionQuantity, transactionType, transactionDateTime);

        ItemAggregateDto itemAggregateDto = ItemAggregateDto.of(itemDto, stockDto, List.of(stockTransactionDto));

        // when
        ItemEntity actual = ItemEntity.buildFrom(itemAggregateDto);

        // then
        assertThat(actual.getId()).isEqualTo(10L);
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getPrice()).isEqualTo(price);
        assertThat(actual.getDescription()).isEqualTo(description);
        assertThat(actual.getStock().getId()).isEqualTo(10L);
        assertThat(actual.getStock().getQuantity()).isEqualTo(quantity);
        assertThat(actual.getStock().getTransactions()).hasSize(1)
                .extracting("id", "quantity", "transactionType", "transactionDateTime")
                .contains(
                        tuple(null, transactionQuantity, transactionType, transactionDateTime)
                );
    }

    @DisplayName("ItemEntity를 toItemWithStockDto로 변환한다.")
    @Test
    void toItemWithStockDto() {
        // given
        ItemEntity itemEntity = buildItemEntityForTest();

        StockEntity stockEntity = buildStockEntityForTest();

        itemEntity.bind(stockEntity);

        // when
        ItemWithStockDto actual = itemEntity.toItemWithStockDto();

        // then
        assertThat(actual.item().id()).isEqualTo(itemEntity.getId());
        assertThat(actual.item().name()).isEqualTo(itemEntity.getName());
        assertThat(actual.item().price()).isEqualTo(itemEntity.getPrice());
        assertThat(actual.item().description()).isEqualTo(itemEntity.getDescription());

        assertThat(actual.stock().id()).isEqualTo(stockEntity.getId());
        assertThat(actual.stock().quantity()).isEqualTo(stockEntity.getQuantity());
    }

    private static ItemEntity buildItemEntityForTest() {
        return ItemEntity.builder()
                .id(1L)
                .name("itemA")
                .price(1000L)
                .description("this is ItemA")
                .build();
    }

    private static StockEntity buildStockEntityForTest() {
        return StockEntity.builder()
                .id(1L)
                .quantity(5000L)
                .build();
    }
}