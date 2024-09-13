package com.shippingflow.core.aggregate.orderitem;

import com.shippingflow.core.aggregate.item.root.Item;
import com.shippingflow.core.aggregate.order.local.OrderItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderItemTest {

    @DisplayName("주문 상품을 생성한다.")
    @Test
    void createOrderItem() {
        // given
        Item item = createTestItem();
        int orderQuantity = 10;

        // when
        OrderItem actual = OrderItem.createOrderItem(item, orderQuantity);

        // then
        assertThat(actual.getItem()).isEqualTo(item);
        assertThat(actual.getOrderQuantity()).isEqualTo(orderQuantity);
    }

    @DisplayName("주문 상품 시 상품은 필수이다.")
    @Test
    void createOrderItem_itemMustNotBeNull() {
        // given
        Item item = null;
        int orderQuantity = 10;

        // when & then
        assertThatThrownBy(() -> OrderItem.createOrderItem(item, orderQuantity))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("상품은 필수입니다.");
    }

    @DisplayName("주문 상품 시 상품 주문 갯수는 1개 이상이어야 한다.")
    @Test
    void createOrderItem_orderQuantityMustBeMoreThan1() {
        // given
        Item item = createTestItem();
        int orderQuantity = 0;

        // when & then
        assertThatThrownBy(() -> OrderItem.createOrderItem(item, orderQuantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 주문 갯수는 1개 이상이어야 합니다.");
    }

    private static Item createTestItem() {
        return Item.builder()
                .id(1L)
                .name("itemA")
                .build();
    }

}