//package com.shippingflow.infrastructure.db.jpa.item.repository;
//
//import com.shippingflow.core.aggregate.item.root.Item;
//import com.shippingflow.infrastructure.db.jpa.item.ItemEntity;
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
//@Import(ItemReaderJpaRepository.class)
//@DataJpaTest
//class ItemReaderJpaRepositoryTest {
//
//    @Autowired
//    ItemJpaRepository itemJpaRepository;
//
//    @Autowired
//    ItemReaderJpaRepository itemReaderJpaRepository;
//
//    @DisplayName("중복된 상품명이 있으면 true를 반환한다.")
//    @Test
//    void existsByName_shouldReturnTrueWhenDuplicateNameExists() {
//        // given
//        String name = "ItemA";
//        long price = 1000L;
//        String description = "This is ItemA";
//        Item item = Item.create(name, price, description);
//        itemJpaRepository.save(ItemEntity.create(item));
//
//        // when
//        boolean actual = itemReaderJpaRepository.existsByName(name);
//
//        // then
//        assertThat(actual).isTrue();
//    }
//
//    @DisplayName("중복된 상품명이 없으면 false를 반환한다.")
//    @Test
//    void existsByName_shouldReturnFalseWhenDuplicateNameDoesNotExists() {
//        // given
//        String name = "ItemA";
//        long price = 1000L;
//        String description = "This is ItemA";
//        Item item = Item.create(name, price, description);
//        itemJpaRepository.save(ItemEntity.create(item));
//
//        // when
//        boolean actual = itemReaderJpaRepository.existsByName("ItemB");
//
//        // then
//        assertThat(actual).isFalse();
//    }
//}