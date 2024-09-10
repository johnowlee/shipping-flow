package com.shippingflow.core.domain.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StockTransactionTest {

    @DisplayName("재고내역에 재고가 할당된다.")
    @Test
    void assignedTo() {
        // given
        StockTransaction stockTransaction = StockTransaction.builder()
                .id(1L)
                .transactionType(StockTransactionType.INCREASE)
                .quantity(100L)
                .build();
        Stock stock = Stock.builder().id(1L).quantity(300L).build();

        // when
        stockTransaction.assignedTo(stock);

        // then
        assertThat(stockTransaction.getStock()).isEqualTo(stock);
    }
}