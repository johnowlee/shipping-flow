package com.shippingflow.core.domain.stock;

import com.shippingflow.core.domain.item.Item;
import com.shippingflow.core.exception.DomainException;
import com.shippingflow.core.exception.error.StockError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

class StockTest {

    @DisplayName("제고에 상품이 할당된다.")
    @Test
    void assignedTo() {
        // given
        Item item = Item.createItem(1L, "ItemA", 1000L, "This is ItemA");
        Stock stock = Stock.builder().build();

        // when
        stock.assignedTo(item);

        // then
        assertThat(stock.getItem()).isEqualTo(item);
    }

    @DisplayName("재고에 상품이 이미 있을 경우 상품이 할당되지 않는다.")
    @Test
    void assignedTo_shouldNotAssignedWhenStockIsAlreadyAssigned() {
        // given
        Item oldItem = Item.createItem(1L, "ItemA", 1000L, "This is ItemA");
        Stock stock = Stock.builder().build();
        stock.assignedTo(oldItem);

        // when
        Item newItem = Item.createItem(2L, "ItemB", 3000L, "This is ItemB");
        stock.assignedTo(newItem);

        // then
        assertThat(stock.getItem()).isNotEqualTo(newItem);
        assertThat(stock.getItem()).isEqualTo(oldItem);
    }

    @DisplayName("재고에 재고내역을 추가한다.")
    @Test
    void addTransaction() {
        // given
        StockTransaction stockTransaction = StockTransaction.builder()
                .id(1L)
                .transactionType(StockTransactionType.INCREASE)
                .quantity(100L)
                .build();
        Stock stock = Stock.builder().quantity(300L).build();

        // when
        stock.addTransaction(stockTransaction);

        // then
        assertThat(stock.getTransactions()).hasSize(1)
                .extracting("id", "transactionType", "quantity")
                .contains(tuple(1L, StockTransactionType.INCREASE, 100L));
    }

    @DisplayName("재고에 재고내역 추가 시 중복 내역이 있으면 제거하고 다시 추가한다.")
    @Test
    void addTransaction_shouldRemoveOldTransactionAndAddNewOneWhenDuplicateOneExists() {
        // given
        StockTransaction oldTransaction = StockTransaction.builder()
                .id(1L)
                .transactionType(StockTransactionType.INCREASE)
                .quantity(100L)
                .build();
        Stock stock = Stock.builder().quantity(300L).build();
        stock.addTransaction(oldTransaction);

        StockTransaction duplicateTransaction = StockTransaction.builder()
                .id(1L)
                .transactionType(StockTransactionType.DECREASE)
                .quantity(300L)
                .build();

        // when
        stock.addTransaction(duplicateTransaction);

        // then
        assertThat(oldTransaction).isEqualTo(duplicateTransaction);
        assertThat(stock.getTransactions()).hasSize(1)
                .extracting("id", "transactionType", "quantity")
                .contains(tuple(1L, StockTransactionType.DECREASE, 300L));
    }

    @DisplayName("재고 수량을 증가시킨다.")
    @Test
    void increase() {
        // given
        Stock stock = Stock.builder()
                .quantity(100)
                .build();

        // when
        stock.increase(300);

        // then
        assertThat(stock.getQuantity()).isEqualTo(100 + 300);
    }

    @DisplayName("재고 수량을 감소시킨다.")
    @Test
    void decrease() {
        // given
        Stock stock = Stock.builder()
                .quantity(500)
                .build();

        // when
        stock.decrease(200);

        // then
        assertThat(stock.getQuantity()).isEqualTo(500 - 200);
    }

    @DisplayName("재고가 감소시키는 수량 보다 적으면 예외가 발생한다.")
    @Test
    void decrease_shouldThrowExceptionWhenStockLessThanDecreaseQuantity() {
        // given
        Stock stock = Stock.builder()
                .quantity(1)
                .build();

        // when & then
        assertThatThrownBy(() -> stock.decrease(2))
                        .isInstanceOf(DomainException.class)
                        .hasMessage(StockError.STOCK_SHORTAGE.getMessage());
    }
}