package com.shippingflow.core.aggregate.domain.item.local;

import com.shippingflow.core.aggregate.domain.item.repository.dto.StockTransactionDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

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

    @DisplayName("StockTransactionDto를 StockTransaction으로 변환한다.")
    @Test
    void fromStockTransactionDto() {
        // given
        long transactionId = 1L;
        long quantity = 1000L;
        StockTransactionType transactionType = StockTransactionType.INCREASE;
        LocalDateTime transactionDateTime = LocalDateTime.of(2024, 9, 20, 23, 30);
        StockTransactionDto stockTransactionDto = new StockTransactionDto(transactionId, quantity, transactionType, transactionDateTime);

        // when
        StockTransaction actual = StockTransaction.from(stockTransactionDto);

        // then
        assertThat(actual.getId()).isEqualTo(transactionId);
        assertThat(actual.getQuantity()).isEqualTo(quantity);
        assertThat(actual.getTransactionType()).isEqualTo(transactionType);
        assertThat(actual.getTransactionDateTime()).isEqualTo(transactionDateTime);
    }
}