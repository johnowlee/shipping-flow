package com.shippingflow.infrastructure.db.item.adapter;

import com.shippingflow.infrastructure.db.item.jpa.entity.ItemEntity;
import com.shippingflow.infrastructure.db.item.jpa.entity.StockEntity;
import com.shippingflow.infrastructure.db.item.jpa.repository.ItemJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ActiveProfiles("local")
@Import({ItemReaderAdapter.class})
@DataJpaTest
class ItemReaderAdapterTest {

    @Autowired
    ItemJpaRepository itemJpaRepository;

    @Autowired
    ItemReaderAdapter itemReaderAdapter;

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
        boolean actual = itemReaderAdapter.existsByName(name);

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
        boolean actual = itemReaderAdapter.existsByName("ItemB");

        // then
        assertThat(actual).isFalse();
    }

    @DisplayName("ItemEntity의 ID로 ItemEntity를 조회한다.")
    @Test
    void findItemWithStockById() {
        // given
        String name = "ItemA";
        long price = 1000L;
        String description = "This is ItemA";
        ItemEntity itemEntity = createNewItemForTest(name, price, description);

        StockEntity stockEntity = createNewStockForTest(5000L);

        itemEntity.bind(stockEntity);

        ItemEntity savedItemEntity = itemJpaRepository.save(itemEntity);

        // when
        Optional<ItemEntity> actual = itemReaderAdapter.findItemById(savedItemEntity.getId());

        // then
        assertThat(actual).isPresent();

        ItemEntity actualItem = actual.get();
        assertThat(actualItem.getId()).isEqualTo(savedItemEntity.getId());
        assertThat(actualItem.getName()).isEqualTo(savedItemEntity.getName());
        assertThat(actualItem.getPrice()).isEqualTo(savedItemEntity.getPrice());
        assertThat(actualItem.getDescription()).isEqualTo(savedItemEntity.getDescription());

        assertThat(actualItem.getStock().getId()).isEqualTo(savedItemEntity.getStock().getId());
        assertThat(actualItem.getStock().getQuantity()).isEqualTo(savedItemEntity.getStock().getQuantity());
    }

    @DisplayName("ItemEntity의 ID로 ItemEntity를 조회 시 없으면 빈 Optional을 반환한다.")
    @Test
    void findItemWithStockById_shouldReturnEmptyOptionalWhenItemNotFoundById() {
        // given
        String name = "ItemA";
        long price = 1000L;
        String description = "This is ItemA";
        ItemEntity itemEntity = createNewItemForTest(name, price, description);

        StockEntity stockEntity = createNewStockForTest(5000L);

        itemEntity.bind(stockEntity);

        ItemEntity savedItemEntity = itemJpaRepository.save(itemEntity);

        // when
        Optional<ItemEntity> actual = itemReaderAdapter.findItemById(999L);

        // then
        assertThat(actual).isEmpty();
        assertThat(savedItemEntity.getId()).isNotEqualTo(999L);
    }

    @DisplayName("페이징 처리 된 ItemEntity 목록과 Page객체를 조회한다.")
    @Test
    void findAllItems() {
        // given
        String name = "ItemA";
        long price = 1000L;
        String description = "This is ItemA";
        ItemEntity itemEntity = createNewItemForTest(name, price, description);
        long quantity = 5000L;
        StockEntity stockEntity = createNewStockForTest(quantity);

        itemEntity.bind(stockEntity);

        itemJpaRepository.save(itemEntity);

        Pageable pageable = PageRequest.of(0, 5);

        // when
        Page<ItemEntity> actual = itemReaderAdapter.findAllItems(pageable);

        // then
        assertThat(actual.getNumber()).isEqualTo(0);
        assertThat(actual.getSize()).isEqualTo(5);
        assertThat(actual.getTotalPages()).isEqualTo(1);
        assertThat(actual.getTotalElements()).isEqualTo(1);

        List<ItemEntity> itemEntities = actual.getContent();
        assertThat(itemEntities)
                .hasSize(1)
                .extracting(
                        ItemEntity::getName,
                        ItemEntity::getPrice,
                        ItemEntity::getDescription,
                        item -> item.getStock().getQuantity()
                )
                .containsExactly(
                        tuple(name, price, description, quantity)
                );
    }

    private static ItemEntity createNewItemForTest(String name, long price, String description) {
        return ItemEntity.builder()
                .name(name)
                .price(price)
                .description(description)
                .build();
    }

    private static StockEntity createNewStockForTest(long quantity) {
        return StockEntity.builder()
                .quantity(quantity)
                .build();
    }
}