package com.shippingflow.infrastructure.db.item.adapter;

import com.shippingflow.core.domain.aggregate.item.model.local.StockTransactionType;
import com.shippingflow.infrastructure.db.item.entity.ItemEntity;
import com.shippingflow.infrastructure.db.item.entity.StockEntity;
import com.shippingflow.infrastructure.db.item.entity.StockTransactionEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ActiveProfiles("local")
@Import({ItemWriterAdapter.class})
@DataJpaTest
class ItemWriterAdapterTest {

    @Autowired
    ItemJpaRepository itemJpaRepository;

    @Autowired
    ItemWriterAdapter itemWriterAdapter;

    @DisplayName("ItemEntity를 저장한다.")
    @Test
    void save() {
        // given
        String name = "itemA";
        long price = 1000L;
        String description = "this is ItemA";
        ItemEntity itemEntity = buildItemEntityForTest(null, name, price, description);

        long quantity = 5000L;
        StockEntity stockEntity = buildStockEntityForTest(null, quantity);

        StockTransactionType transactionType = StockTransactionType.INCREASE;
        long transactionQuantity = 5000L;
        LocalDateTime transactionDateTime = LocalDateTime.now();
        StockTransactionEntity stockTransactionEntity = createStockTransactionEntityForTest(transactionType, transactionQuantity, transactionDateTime);

        stockEntity.addTransaction(stockTransactionEntity);
        itemEntity.bind(stockEntity);

        // when
        itemWriterAdapter.save(itemEntity);

        // then
        List<ItemEntity> itemEntities = itemJpaRepository.findAll();
        assertThat(itemEntities)
                .hasSize(1)
                .extracting("name", "price", "description", "stock.quantity")
                .containsExactly(
                        tuple(name, price, description, quantity)
                );

        assertThat(itemEntities.get(0).getStock().getTransactions())
                .hasSize(1)
                .first()
                .extracting("transactionType", "quantity", "transactionDateTime")
                .containsExactly(transactionType, transactionQuantity, transactionDateTime);
    }

    @DisplayName("ItemEntity의 재고를 수정하고 입출고내역을 저장한다.")
    @Test
    void update() {
        // given
        // 상품 저장
        String name = "itemA";
        long price = 1000L;
        String description = "this is ItemA";
        ItemEntity itemEntity = buildItemEntityForTest(null, name, price, description);

        long quantity = 5000L;
        StockEntity stockEntity = buildStockEntityForTest(null, quantity);

        StockTransactionType transactionType = StockTransactionType.INCREASE;
        long transactionQuantity = 5000L;
        LocalDateTime transactionDateTime = LocalDateTime.now();
        StockTransactionEntity stockTransactionEntity = createStockTransactionEntityForTest(transactionType, transactionQuantity, transactionDateTime);

        stockEntity.addTransaction(stockTransactionEntity);
        itemEntity.bind(stockEntity);


        ItemEntity savedItemEntity = itemWriterAdapter.save(itemEntity);
        StockEntity savedStockEntity = savedItemEntity.getStock();

        // 저장된 상품 수정
        long updateQuantity = 2000L;
        StockEntity updateStockEntity = buildStockEntityForTest(savedStockEntity.getId(), savedStockEntity.getQuantity() - updateQuantity);

        StockTransactionType updateTransactionType = StockTransactionType.DECREASE;
        long updateTransactionQuantity = 2000L;
        LocalDateTime updateTransactionDateTime = LocalDateTime.now();
        StockTransactionEntity updateStockTransactionEntity = createStockTransactionEntityForTest(updateTransactionType, updateTransactionQuantity, updateTransactionDateTime);

        updateStockEntity.addTransactions(List.of(stockTransactionEntity, updateStockTransactionEntity));
        savedItemEntity.bind(updateStockEntity);

        // when
        itemWriterAdapter.save(savedItemEntity);

        // then
        List<ItemEntity> itemEntities = itemJpaRepository.findAll();
        assertThat(itemEntities)
                .hasSize(1)
                .extracting("name", "price", "description", "stock.quantity")
                .containsExactly(
                        tuple(name, price, description, quantity - updateQuantity)
                );

        assertThat(itemEntities.get(0).getStock().getTransactions())
                .hasSize(2)
                .extracting("transactionType", "quantity", "transactionDateTime")
                .containsExactly(
                        tuple(transactionType, transactionQuantity, transactionDateTime),
                        tuple(updateTransactionType, updateTransactionQuantity, updateTransactionDateTime)
                );
    }

    private static StockTransactionEntity createStockTransactionEntityForTest(StockTransactionType transactionType, long transactionQuantity, LocalDateTime transactionDateTime) {
        return StockTransactionEntity.builder()
                .transactionType(transactionType)
                .quantity(transactionQuantity)
                .transactionDateTime(transactionDateTime)
                .build();
    }

    private static StockEntity buildStockEntityForTest(Long id, long quantity) {
        return StockEntity.builder()
                .id(id)
                .quantity(quantity)
                .build();
    }

    private static ItemEntity buildItemEntityForTest(Long id, String name, long price, String description) {
        return ItemEntity.builder()
                .id(id)
                .name(name)
                .price(price)
                .description(description)
                .build();
    }
}