package com.shippingflow.core.aggregate.item;

import com.shippingflow.core.aggregate.item.root.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ItemTest {

//    @DisplayName("상품에 재고 객체가 이미 지정 되어 있으면 재고를 새로 할당하지 않는다.")
//    @Test
//    void assignStock_shouldNotAssignWhenItemAlreadyAssignedStock() {
//        // given
//        Item item = Item.builder().id(1L).name("itemA").price(1000L).build();
//        Stock oldStock = Stock.builder().id(1L).quantity(100).build();
//        item.assignStock(oldStock);
//
//        // when
//        Stock newStock = Stock.builder().id(2L).quantity(300).build();
//        item.assignStock(newStock);
//
//        // then
//        assertThat(item.getStock()).isNotEqualTo(newStock);
//        assertThat(item.getStock()).isEqualTo(oldStock);
//        assertThat(oldStock.getItem()).isEqualTo(item);
//    }
//
//    @DisplayName("상품에 재고 객체를 지정하고 해당 재고에 상품이 할당된다.")
//    @Test
//    void assignStock() {
//        // given
//        Item item = Item.builder().id(1L).name("itemA").price(1000L).build();
//        Stock stock = Stock.builder().id(1L).quantity(100).build();
//
//        // when
//        item.assignStock(stock);
//
//        // then
//        assertThat(item.getStock()).isEqualTo(stock);
//        assertThat(stock.getItem()).isEqualTo(item);
//    }

    @DisplayName("신규 상품을 생성하면 재고 객체도 함께 생성된다.")
    @Test
    void create() {
        // given
        String name = "itemA";
        Long price = 10_000L;
        String description = "this is itemA";

        // when
        Item actual = Item.create(name, price, description);

        // then
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getPrice()).isEqualTo(price);
        assertThat(actual.getDescription()).isEqualTo(description);
        assertThat(actual.getStock()).isNotNull();
        assertThat(actual.getStock().getItem()).isEqualTo(actual);
    }

}