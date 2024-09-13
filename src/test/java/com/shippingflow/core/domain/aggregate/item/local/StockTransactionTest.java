package com.shippingflow.core.domain.aggregate.item.local;

import com.shippingflow.core.domain.aggregate.item.local.Stock;
import com.shippingflow.core.domain.aggregate.item.local.StockTransaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StockTransactionTest {

    @DisplayName("재고 연관관계를 설정한다.")
    @Test
    void bindTo() {
        // given
        Stock stock = Stock.builder().id(1L).quantity(100L).build();
        StockTransaction transaction = StockTransaction.builder().id(1L).build();

        // when
        transaction.bindTo(stock);

        // then
        assertThat(transaction.getStock()).isEqualTo(stock);
        assertThat(transaction.getStock().getId()).isEqualTo(stock.getId());
        assertThat(transaction.getStock().getQuantity()).isEqualTo(stock.getQuantity());
    }

}