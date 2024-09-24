package com.shippingflow.infrastructure.db.item.jpa.repository;

import com.shippingflow.core.domain.aggregate.item.dto.ItemDto;
import com.shippingflow.core.domain.aggregate.item.dto.ItemWithStockDto;
import com.shippingflow.core.domain.aggregate.item.dto.StockDto;
import com.shippingflow.infrastructure.db.item.jpa.entity.ItemEntity;
import com.shippingflow.infrastructure.db.item.jpa.entity.StockEntity;
import com.shippingflow.infrastructure.service.item.ItemEntityPageMapper;
import com.shippingflow.infrastructure.service.support.paging.PageableFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@Import({ItemReaderJpaRepository.class, ItemEntityPageMapper.class, PageableFactory.class})
@DataJpaTest
class ItemReaderJpaRepositoryTest {

    @Autowired
    ItemJpaRepository itemJpaRepository;

    @Autowired
    ItemEntityPageMapper itemEntityPageMapper;

    @Autowired
    PageableFactory pageableFactory;

    @Autowired
    ItemReaderJpaRepository itemReaderJpaRepository;

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
        boolean actual = itemReaderJpaRepository.existsByName(name);

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
        boolean actual = itemReaderJpaRepository.existsByName("ItemB");

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
        Optional<ItemWithStockDto> actual = itemReaderJpaRepository.findItemWithStockById(savedItemEntity.getId());

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
        Optional<ItemWithStockDto> actual = itemReaderJpaRepository.findItemWithStockById(999L);

        // then
        assertThat(actual).isEmpty();
        assertThat(savedItemEntity.getId()).isNotEqualTo(999L);
    }

    private static ItemEntity createNewItemForTest(String name, long price, String description) {
        return ItemEntity.builder()
                .name(name)
                .price(price)
                .description(description)
                .build();
    }
}