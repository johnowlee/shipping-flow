package com.shippingflow.core.domain.aggregate.order;

import com.shippingflow.core.domain.aggregate.customer.Customer;
import com.shippingflow.core.domain.aggregate.item.root.Item;
import com.shippingflow.core.domain.aggregate.order.root.Order;
import com.shippingflow.core.domain.aggregate.order.local.OrderItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTest {

    @DisplayName("주문 객체를 생성한다.")
    @Test
    void createOrder() {
        Customer customer = createTestCustomer();

        Item item = Item.builder()
                .id(1L)
                .name("itemA")
                .price(10_000L)
                .build();

        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .orderQuantity(1)
                .build();

        // when
        Order actual = Order.createOrder(customer, Set.of(orderItem));

        // then
        assertThat(actual.getCustomer()).isEqualTo(customer);
        assertThat(actual.getOrderItems().size()).isEqualTo(1);
        assertThat(actual.getOrderItems().contains(orderItem)).isTrue();
    }

    @DisplayName("주문 객체를 생성할때 고객은 필수이다.")
    @Test
    void createOrder_customerMustNotBeNull() {
        // when & then
        assertThatThrownBy(() -> Order.createOrder(null, null))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("고객은 필수 입니다.");
    }

    @DisplayName("주문 객체를 생성할때 주문 상품 목록은 필수이다.")
    @Test
    void createOrder_orderItemsMustNotBeNull() {
        Customer customer = createTestCustomer();

        // when & then
        assertThatThrownBy(() -> Order.createOrder(customer, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상품 목록은 필수 입니다.");
    }

    @DisplayName("주문 객체를 생성할때 최소 주문 상품 목록은 1개 이상이다.")
    @Test
    void createOrder_orderItemsMustNotBeEmpty() {
        Customer customer = createTestCustomer();

        // when & then
        assertThatThrownBy(() -> Order.createOrder(customer, new HashSet<>()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("최소 주문 상품은 1개 이상입니다.");
    }

    private static Customer createTestCustomer() {
        return Customer.builder()
                .id(1L)
                .name("john")
                .build();
    }
}
