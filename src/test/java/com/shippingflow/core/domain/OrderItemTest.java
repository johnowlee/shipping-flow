package com.shippingflow.core.domain;

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

    @DisplayName("상품 객체와 주문 갯수가 같다면 두 인스턴스는 동등하다.")
    @Test
    void equals() {
        // given
        Item item = createTestItem();
        int orderQuantity = 10;

        // when
        OrderItem orderItemA = OrderItem.createOrderItem(item, orderQuantity);
        OrderItem orderItemB = OrderItem.createOrderItem(item, orderQuantity);

        // then
        assertThat(orderItemA.getItem()).isEqualTo(orderItemB.getItem());
        assertThat(orderItemA.getOrderQuantity()).isEqualTo(orderItemB.getOrderQuantity());
        assertThat(orderItemA).isEqualTo(orderItemB);
    }

    private static Item createTestItem() {
        return Item.builder()
                .id(1L)
                .name("itemA")
                .build();
    }

}