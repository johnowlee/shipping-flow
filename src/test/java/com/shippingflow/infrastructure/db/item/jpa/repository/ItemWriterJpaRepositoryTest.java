package com.shippingflow.infrastructure.db.item.jpa.repository;

import com.shippingflow.core.domain.aggregate.item.dto.*;
import com.shippingflow.core.domain.aggregate.item.model.local.StockTransactionType;
import com.shippingflow.infrastructure.db.item.jpa.entity.ItemEntity;
import com.shippingflow.infrastructure.db.item.jpa.entity.StockEntity;
import com.shippingflow.infrastructure.db.item.jpa.entity.StockTransactionEntity;
import com.shippingflow.infrastructure.db.item.jpa.repository.ItemJpaRepository;
import com.shippingflow.infrastructure.db.item.jpa.repository.ItemWriterJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@Import(ItemWriterJpaRepository.class)
@DataJpaTest
class ItemWriterJpaRepositoryTest {

    @Autowired
    ItemJpaRepository itemJpaRepository;

    @Autowired
    ItemWriterJpaRepository itemWriterJpaRepository;

    @DisplayName("새로운 ItemEntity를 저장한다.")
    @Test
    void saveNewItem() {
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
        ItemWithStockDto actual = itemWriterJpaRepository.saveNewItem(itemAggregateDto);

        // then
        ItemDto actualItemDto = actual.item();
        assertThat(actualItemDto.id()).isNotNull();
        assertThat(actualItemDto.name()).isEqualTo(name);
        assertThat(actualItemDto.price()).isEqualTo(price);
        assertThat(actualItemDto.description()).isEqualTo(description);

        StockDto actualStockDto = actual.stock();
        assertThat(actualStockDto.id()).isNotNull();
        assertThat(actualStockDto.quantity()).isEqualTo(quantity);

        Optional<ItemEntity> itemEntity = itemJpaRepository.findById(actualItemDto.id());
        assertThat(itemEntity).isPresent();
        List<StockTransactionEntity> transactions = itemEntity.get().getStock().getTransactions();
        assertThat(transactions).hasSize(1);
        assertThat(transactions.get(0).getId()).isNotNull();
        assertThat(transactions.get(0).getQuantity()).isEqualTo(quantity);
        assertThat(transactions.get(0).getTransactionType()).isEqualTo(transactionType);
        assertThat(transactions.get(0).getTransactionDateTime()).isEqualTo(transactionDateTime);
    }

    @DisplayName("ItemEntity의 재고를 업데이트한다.")
    @Test
    void updateStock() {
        // given
        String name = "newItemA";
        long price = 1000L;
        String description = "this is newItemA";
        ItemEntity itemEntity = ItemEntity.builder()
                .name(name)
                .price(price)
                .description(description)
                .build();

        long originalQuantity = 5000L;
        StockEntity stockEntity = StockEntity.builder()
                .quantity(originalQuantity)
                .build();

        itemEntity.bind(stockEntity);
        ItemEntity savedItem = itemJpaRepository.save(itemEntity);

        ItemDto itemDto = ItemDto.of(savedItem.getId(), savedItem.getName(), savedItem.getPrice(), savedItem.getDescription());

        long increaseQuantity = 10_000L;
        StockDto stockDto = StockDto.of(savedItem.getStock().getId(), savedItem.getStock().getQuantity() + increaseQuantity);

        StockTransactionType transactionType = StockTransactionType.INCREASE;
        LocalDateTime transactionDateTime = LocalDateTime.now();
        StockTransactionDto stockTransactionDto = StockTransactionDto.of(null, increaseQuantity, transactionType, transactionDateTime);

        ItemAggregateDto itemAggregateDto = ItemAggregateDto.of(itemDto, stockDto, List.of(stockTransactionDto));

        // when
        ItemWithStockDto actual = itemWriterJpaRepository.updateStock(itemAggregateDto);

        // then
        ItemDto actualItemDto = actual.item();
        assertThat(actualItemDto.id()).isEqualTo(savedItem.getId());
        assertThat(actualItemDto.name()).isEqualTo(savedItem.getName());
        assertThat(actualItemDto.price()).isEqualTo(savedItem.getPrice());
        assertThat(actualItemDto.description()).isEqualTo(savedItem.getDescription());

        StockDto actualStockDto = actual.stock();
        assertThat(actualStockDto.id()).isEqualTo(savedItem.getStock().getId());
        assertThat(actualStockDto.quantity()).isEqualTo(originalQuantity + increaseQuantity);

        Optional<ItemEntity> foundItemEntity = itemJpaRepository.findById(actualItemDto.id());
        assertThat(foundItemEntity).isPresent();
        List<StockTransactionEntity> transactions = foundItemEntity.get().getStock().getTransactions();
        assertThat(transactions).hasSize(1);
        assertThat(transactions.get(0).getId()).isNotNull();
        assertThat(transactions.get(0).getQuantity()).isEqualTo(increaseQuantity);
        assertThat(transactions.get(0).getTransactionType()).isEqualTo(transactionType);
        assertThat(transactions.get(0).getTransactionDateTime()).isEqualTo(transactionDateTime);
    }
}