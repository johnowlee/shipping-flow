package com.shippingflow.core.domain.aggregate.item.local;

import com.shippingflow.core.domain.aggregate.item.root.Item;
import com.shippingflow.core.exception.DomainException;
import com.shippingflow.core.exception.error.ItemError;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.BDDAssertions.tuple;

class StockTest {
    
    @DisplayName("재고에 상품 연관관계를 설정한다.")
    @Test
    void bindTo() {
        // given
        Item item = Item.builder().id(1L).name("itemA").price(1000L).description("this is ItemA").build();
        Stock stock = buildStock(1L, 50L);
        
        // when
        stock.bindTo(item);
        
        // then
        Assertions.assertThat(stock.getItem()).isEqualTo(item);
        Assertions.assertThat(stock.getItem().getName()).isEqualTo(item.getName());
        Assertions.assertThat(stock.getItem().getPrice()).isEqualTo(item.getPrice());
        Assertions.assertThat(stock.getItem().getDescription()).isEqualTo(item.getDescription());
    }

    @DisplayName("재고 수량을 증가시킨다.")
    @Test
    void increase() {
        // given
        Stock stock = buildStock(1L, 10L);

        // when
        stock.increase(30L);
        stock.increase(60L);

        // then
        Assertions.assertThat(stock.getQuantity()).isEqualTo(10L + 30L + 60L);
    }

    @DisplayName("재고 수량을 감소시킨다.")
    @Test
    void decrease() {
        // given
        Stock stock = buildStock(1L, 100L);

        // when
        stock.decrease(30L);
        stock.decrease(60L);

        // then
        Assertions.assertThat(stock.getQuantity()).isEqualTo(100L - 30L - 60L);
    }

    @DisplayName("재고 수량 감소 시 남아있는 재고의 양이 부족하면 예외가 발생한다.")
    @Test
    void decrease_shouldThrowExceptionWhenQuantityLessThanDecrease() {
        // given
        Stock stock = buildStock(1L, 100L);

        // when & then
        stock.decrease(100L);
        Assertions.assertThatThrownBy(() -> stock.decrease(1L))
                        .isInstanceOf(DomainException.class)
                        .hasMessage(ItemError.STOCK_SHORTAGE.getMessage());
    }

    @DisplayName("재고 입출고 내역을 기록한다.")
    @Test
    void recordTransaction() {
        // given
        Stock stock = buildStock(1L, 0);
        StockTransactionType transactionType = StockTransactionType.INCREASE;
        long quantity = 1L;
        LocalDateTime transactionDateTime = LocalDateTime.of(2024, 9, 13, 12, 0, 0);

        // when
        stock.recordTransaction(transactionType, quantity, transactionDateTime);

        // then
        Assertions.assertThat(stock.getTransactions()).hasSize(1)
                .extracting("stock", "transactionType", "quantity", "transactionDateTime")
                .contains(
                        tuple(stock, transactionType, quantity, transactionDateTime)
                );
    }

    @DisplayName("재고 입출고 내역 입력 시 입출고 수량은 1개 이상이어야 한다.")
    @Test
    void recordTransaction_shouldThrowExceptionWhenTransactionQuantityLessThanOne() {
        // given
        Stock stock = buildStock(1L, 0);
        StockTransactionType transactionType = StockTransactionType.INCREASE;
        long quantity = 0L;
        LocalDateTime transactionDateTime = LocalDateTime.of(2024, 9, 13, 12, 0, 0);

        // when & then
        Assertions.assertThatThrownBy(() -> stock.recordTransaction(transactionType, quantity, transactionDateTime))
                        .isInstanceOf(DomainException.class)
                        .hasMessage(ItemError.INSUFFICIENT_QUANTITY.getMessage());
    }

    @DisplayName("재고 입출고 내역을 추가한다.")
    @Test
    void addTransaction() {
        // given
        Stock stock = buildStock(1L, 0);
        long transactionId = 1L;
        StockTransactionType transactionType = StockTransactionType.INCREASE;
        long quantity = 1000L;
        LocalDateTime transactionDateTime = LocalDateTime.of(2024, 9, 13, 12, 0, 0);
        StockTransaction stockTransaction = StockTransaction.builder()
                .id(transactionId)
                .transactionType(transactionType)
                .quantity(quantity)
                .transactionDateTime(transactionDateTime)
                .build();

        // when
        stock.addTransaction(stockTransaction);

        // then
        Assertions.assertThat(stock.getTransactions()).hasSize(1)
                .extracting("id", "transactionType", "quantity", "transactionDateTime")
                .contains(
                        tuple(transactionId, transactionType, quantity, transactionDateTime)
                );
    }

    private static Stock buildStock(long id, long quantity) {
        return Stock.builder().id(id).quantity(quantity).build();
    }
}