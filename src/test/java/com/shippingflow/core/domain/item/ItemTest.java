package com.shippingflow.core.domain.item;

import com.shippingflow.core.domain.stock.Stock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ItemTest {

    @DisplayName("상품에 재고 객체를 지정하고 해당 재고에 상품이 할당된다.")
    @Test
    void assignStock() {
        // given
        Item item = Item.builder().id(1L).name("itemA").price(1000L).build();
        Stock stock = Stock.builder().id(1L).quantity(100).build();

        // when
        item.assignStock(stock);

        // then
        assertThat(item.getStock()).isEqualTo(stock);
        assertThat(stock.getItem()).isEqualTo(item);
    }

    @DisplayName("상품에 재고 객체가 이미 지정 되어 있으면 재고를 새로 할당하지 않는다.")
    @Test
    void assignStock_shouldNotAssignWhenItemAlreadyAssignedStock() {
        // given
        Item item = Item.builder().id(1L).name("itemA").price(1000L).build();
        Stock oldStock = Stock.builder().id(1L).quantity(100).build();
        item.assignStock(oldStock);

        // when
        Stock newStock = Stock.builder().id(2L).quantity(300).build();
        item.assignStock(newStock);

        // then
        assertThat(item.getStock()).isNotEqualTo(newStock);
        assertThat(item.getStock()).isEqualTo(oldStock);
        assertThat(oldStock.getItem()).isEqualTo(item);
    }

}