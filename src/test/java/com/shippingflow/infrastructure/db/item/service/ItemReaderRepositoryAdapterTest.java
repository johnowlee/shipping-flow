package com.shippingflow.infrastructure.db.item.service;

import com.shippingflow.core.domain.aggregate.item.dto.ItemDto;
import com.shippingflow.core.domain.aggregate.item.dto.ItemWithStockDto;
import com.shippingflow.core.domain.aggregate.item.dto.StockDto;
import com.shippingflow.core.domain.aggregate.item.dto.StockTransactionDto;
import com.shippingflow.core.domain.aggregate.item.model.local.StockTransactionType;
import com.shippingflow.core.domain.common.pagination.BasicPaginationRequest;
import com.shippingflow.core.domain.common.pagination.PageResponse;
import com.shippingflow.core.domain.common.pagination.SortablePaginationRequest;
import com.shippingflow.infrastructure.common.factory.PageableFactory;
import com.shippingflow.infrastructure.db.item.adapter.repository.ItemJpaRepository;
import com.shippingflow.infrastructure.db.item.entity.ItemEntity;
import com.shippingflow.infrastructure.db.item.entity.StockEntity;
import com.shippingflow.infrastructure.db.item.entity.StockTransactionEntity;
import com.shippingflow.infrastructure.db.item.mapper.ItemEntityPageMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ActiveProfiles("local")
@Transactional
@SpringBootTest
class ItemReaderRepositoryAdapterTest {

    @Autowired
    ItemJpaRepository itemJpaRepository;

    @Autowired
    ItemEntityPageMapper itemEntityPageMapper;

    @Autowired
    PageableFactory pageableFactory;

    @Autowired
    ItemReaderRepositoryAdapter itemReaderRepositoryAdapter;

    @DisplayName("중복된 상품명이 있으면 true를 반환한다.")
    @Test
    void existsByName_shouldReturnTrueWhenDuplicateNameExists() {
        // given
        String name = "ItemA";
        long price = 1000L;
        String description = "This is ItemA";
        ItemEntity item = createNewItemForTest(name, price, description);
        itemJpaRepository.save(item);

        // when
        boolean actual = itemReaderRepositoryAdapter.existsByName(name);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("중복된 상품명이 없으면 false를 반환한다.")
    @Test
    void existsByName_shouldReturnFalseWhenDuplicateNameDoesNotExists() {
        // given
        String name = "ItemA";
        long price = 1000L;
        String description = "This is ItemA";
        ItemEntity itemEntity = createNewItemForTest(name, price, description);
        itemJpaRepository.save(itemEntity);

        // when
        boolean actual = itemReaderRepositoryAdapter.existsByName("ItemB");

        // then
        assertThat(actual).isFalse();
    }

    @DisplayName("ItemEntity의 ID로 ItemEntity를 조회하고 ItemDto와 StockDto 정보를 반환한다.")
    @Test
    void findItemWithStockById() {
        // given
        String name = "ItemA";
        long price = 1000L;
        String description = "This is ItemA";
        ItemEntity itemEntity = createNewItemForTest(name, price, description);

        StockEntity stockEntity = StockEntity.builder()
                .quantity(5000L)
                .build();

        itemEntity.bind(stockEntity);

        ItemEntity savedItemEntity = itemJpaRepository.save(itemEntity);

        // when
        Optional<ItemWithStockDto> actual = itemReaderRepositoryAdapter.findItemWithStockById(savedItemEntity.getId());

        // then
        assertThat(actual).isPresent();

        ItemDto actualItem = actual.get().item();
        assertThat(actualItem.id()).isEqualTo(savedItemEntity.getId());
        assertThat(actualItem.name()).isEqualTo(savedItemEntity.getName());
        assertThat(actualItem.price()).isEqualTo(savedItemEntity.getPrice());
        assertThat(actualItem.description()).isEqualTo(savedItemEntity.getDescription());

        StockDto actualStock = actual.get().stock();
        assertThat(actualStock.id()).isEqualTo(savedItemEntity.getStock().getId());
        assertThat(actualStock.quantity()).isEqualTo(savedItemEntity.getStock().getQuantity());
    }

    @DisplayName("ItemEntity의 ID로 ItemEntity를 조회 시 없으면 빈 Optional을 반환한다.")
    @Test
    void findItemWithStockById_shouldReturnEmptyOptionalWhenItemNotFoundById() {
        // given
        String name = "ItemA";
        long price = 1000L;
        String description = "This is ItemA";
        ItemEntity itemEntity = createNewItemForTest(name, price, description);

        StockEntity stockEntity = StockEntity.builder()
                .quantity(5000L)
                .build();

        itemEntity.bind(stockEntity);

        ItemEntity savedItemEntity = itemJpaRepository.save(itemEntity);

        // when
        Optional<ItemWithStockDto> actual = itemReaderRepositoryAdapter.findItemWithStockById(999L);

        // then
        assertThat(actual).isEmpty();
        assertThat(savedItemEntity.getId()).isNotEqualTo(999L);
    }

    @DisplayName("페이징 요청에 따라 상품 목록과 재고 정보를 반환한다.")
    @Test
    void findAllItemsWithStock() {
        // given
        String name1 = "ItemA";
        String name2 = "ItemB";
        String name3 = "ItemC";
        long price = 1000L;
        String description = "This is ItemA";

        ItemEntity item1 = createNewItemForTest(name1, price, description);
        ItemEntity item2 = createNewItemForTest(name2, price, description);
        ItemEntity item3 = createNewItemForTest(name3, price, description);

        StockEntity stock1 = StockEntity.builder().quantity(100L).build();
        StockEntity stock2 = StockEntity.builder().quantity(200L).build();
        StockEntity stock3 = StockEntity.builder().quantity(300L).build();

        item1.bind(stock1);
        item2.bind(stock2);
        item3.bind(stock3);

        itemJpaRepository.saveAll(List.of(item1, item2, item3));

        SortablePaginationRequest sortablePaginationRequest = SortablePaginationRequest.of(1, 2, "name", "asc");

        // when
        PageResponse<ItemWithStockDto> pageResponse = itemReaderRepositoryAdapter.findAllItemsWithStock(sortablePaginationRequest);

        // then
        assertThat(pageResponse).isNotNull();
        assertThat(pageResponse.pageNumber()).isEqualTo(0);
        assertThat(pageResponse.pageSize()).isEqualTo(2);
        assertThat(pageResponse.totalElements()).isEqualTo(3);
        assertThat(pageResponse.totalPages()).isEqualTo(2);

        assertThat(pageResponse.content())
                .hasSize(2)
                .flatExtracting(
                        item -> item.item().name(),
                        item -> item.stock().quantity()
                )
                .containsExactly("ItemA", 100L, "ItemB", 200L);
    }

    @DisplayName("상품 ID와 페이징 요청에 따라 상품 재고 내역을 반환한다.")
    @Test
    void findStockTransactionsByItemId() {
        // given
        ItemEntity item = createNewItemForTest("ItemA", 1000L, "This is ItemA");
        StockEntity stock = StockEntity.builder().quantity(100L).build();

        StockTransactionEntity tx1 = createNewStockTransactionForTest(1000L, StockTransactionType.INCREASE, LocalDateTime.now());
        StockTransactionEntity tx2 = createNewStockTransactionForTest(500L, StockTransactionType.DECREASE, LocalDateTime.now());

        stock.addTransactions(List.of(tx1, tx2));
        item.bind(stock);
        ItemEntity savedItemEntity = itemJpaRepository.save(item);

        BasicPaginationRequest basicPaginationRequest = BasicPaginationRequest.of(1, 2);

        // when
        PageResponse<StockTransactionDto> actual = itemReaderRepositoryAdapter.findStockTransactionsByItemId(savedItemEntity.getId(), basicPaginationRequest);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.pageNumber()).isEqualTo(0);
        assertThat(actual.pageSize()).isEqualTo(2);
        assertThat(actual.totalElements()).isEqualTo(2);
        assertThat(actual.totalPages()).isEqualTo(1);

        assertThat(actual.content())
                .hasSize(2)
                .extracting("quantity", "transactionType", "transactionDateTime")
                .contains(
                        tuple(tx1.getQuantity(), tx1.getTransactionType(), tx1.getTransactionDateTime()),
                        tuple(tx2.getQuantity(), tx2.getTransactionType(), tx2.getTransactionDateTime())
                );
    }

    private static ItemEntity createNewItemForTest(String name, long price, String description) {
        return ItemEntity.builder()
                .name(name)
                .price(price)
                .description(description)
                .build();
    }

    private static StockTransactionEntity createNewStockTransactionForTest(long transactionQuantity, StockTransactionType transactionType, LocalDateTime transactionDateTime) {
        return StockTransactionEntity.builder()
                .quantity(transactionQuantity)
                .transactionType(transactionType)
                .transactionDateTime(transactionDateTime)
                .build();
    }
}