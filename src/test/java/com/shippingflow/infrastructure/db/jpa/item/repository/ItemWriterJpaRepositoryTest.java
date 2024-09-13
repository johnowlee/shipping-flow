//package com.shippingflow.infrastructure.db.jpa.item.repository;
//
//import com.shippingflow.core.aggregate.item.root.Item;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.ActiveProfiles;
//
//@ActiveProfiles("local")
//@Import(ItemWriterJpaRepository.class)
//@DataJpaTest
//class ItemWriterJpaRepositoryTest {
//
//    @Autowired
//    ItemJpaRepository itemJpaRepository;
//
//    @Autowired
//    ItemWriterJpaRepository itemWriterJpaRepository;
//
//    @DisplayName("상품을 등록한다.")
//    @Test
//    void save() {
//        // given
//        String name = "ItemA";
//        long price = 1000L;
//        String description = "This is ItemA";
//        Item item = Item.create(name, price, description);
//
//        // when
//        Item actual = itemWriterJpaRepository.save(item);
//
//        // then
//        Assertions.assertThat(actual.getId()).isNotNull();
//        Assertions.assertThat(actual.getName()).isEqualTo(name);
//        Assertions.assertThat(actual.getPrice()).isEqualTo(price);
//        Assertions.assertThat(actual.getDescription()).isEqualTo(description);
//    }
//
//}