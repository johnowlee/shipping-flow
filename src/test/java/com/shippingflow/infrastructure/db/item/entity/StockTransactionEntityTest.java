package com.shippingflow.infrastructure.db.item.entity;

import com.shippingflow.core.domain.aggregate.item.dto.StockTransactionDto;
import com.shippingflow.core.domain.aggregate.item.model.local.StockTransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class StockTransactionEntityTest {

    @DisplayName("StockTransactionDto로 부터 새로운 StockTransactionEntity를 생성한다.")
    @Test
    void createFrom() {
        // given
        long transactionQuantity = 5000L;
        StockTransactionType transactionType = StockTransactionType.INCREASE;
        LocalDateTime transactionDateTime = LocalDateTime.now();
        StockTransactionDto stockTransactionDto = StockTransactionDto.of(null, transactionQuantity, transactionType, transactionDateTime);

        // when
        StockTransactionEntity actual = StockTransactionEntity.createFrom(stockTransactionDto);

        // then
        assertThat(actual.getQuantity()).isEqualTo(transactionQuantity);
        assertThat(actual.getTransactionType()).isEqualTo(transactionType);
        assertThat(actual.getTransactionDateTime()).isEqualTo(transactionDateTime);
    }

    @DisplayName("StockTransactionDto로 변환한다.")
    @Test
    void toStockTransactionDto() {
        // given
        long id = 1L;
        long quantity = 1000L;
        StockTransactionType transactionType = StockTransactionType.INCREASE;
        LocalDateTime transactionDateTime = LocalDateTime.now();
        StockTransactionEntity stockTransactionEntity = StockTransactionEntity.builder()
                .id(id)
                .quantity(quantity)
                .transactionType(transactionType)
                .transactionDateTime(transactionDateTime)
                .build();

        // when
        StockTransactionDto actual = stockTransactionEntity.toStockTransactionDto();

        // then
        assertThat(actual.id()).isEqualTo(id);
        assertThat(actual.quantity()).isEqualTo(quantity);
        assertThat(actual.transactionType()).isEqualTo(transactionType);
        assertThat(actual.transactionDateTime()).isEqualTo(transactionDateTime);
    }
}