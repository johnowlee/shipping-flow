package com.shippingflow.infrastructure.service.item;

import com.shippingflow.core.domain.aggregate.item.dto.ItemDto;
import com.shippingflow.core.domain.aggregate.item.dto.ItemWithStockDto;
import com.shippingflow.core.domain.aggregate.item.dto.StockDto;
import com.shippingflow.core.domain.common.pagination.PageResponse;
import com.shippingflow.core.domain.common.pagination.PaginationRequest;
import com.shippingflow.infrastructure.db.item.jpa.entity.ItemEntity;
import com.shippingflow.infrastructure.db.item.jpa.entity.StockEntity;
import com.shippingflow.infrastructure.db.item.jpa.repository.ItemJpaRepository;
import com.shippingflow.infrastructure.service.support.paging.PageableFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@Transactional
@SpringBootTest
class ItemReaderRepositoryHelperTest {

    @Autowired
    ItemJpaRepository itemJpaRepository;

    @Autowired
    ItemEntityPageMapper itemEntityPageMapper;

    @Autowired
    PageableFactory pageableFactory;

    @Autowired
    ItemReaderRepositoryHelper itemReaderRepositoryHelper;

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
        boolean actual = itemReaderRepositoryHelper.existsByName(name);

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
        boolean actual = itemReaderRepositoryHelper.existsByName("ItemB");

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
        Optional<ItemWithStockDto> actual = itemReaderRepositoryHelper.findItemWithStockById(savedItemEntity.getId());

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
        Optional<ItemWithStockDto> actual = itemReaderRepositoryHelper.findItemWithStockById(999L);

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

        PaginationRequest paginationRequest = PaginationRequest.of(1, 2, "name", "asc");

        // when
        PageResponse<ItemWithStockDto> pageResponse = itemReaderRepositoryHelper.findAllItemsWithStock(paginationRequest);

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

    private static ItemEntity createNewItemForTest(String name, long price, String description) {
        return ItemEntity.builder()
                .name(name)
                .price(price)
                .description(description)
                .build();
    }
}