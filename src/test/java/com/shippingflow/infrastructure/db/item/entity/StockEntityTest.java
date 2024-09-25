package com.shippingflow.infrastructure.db.item.entity;

import com.shippingflow.core.domain.aggregate.item.dto.StockDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StockEntityTest {
    @DisplayName("StockDto로 부터 StockEntity를 생성한다.")
    @Test
    void buildFrom() {
        // given
        StockDto stockDto = StockDto.of(1L, 1000L);

        // when
        StockEntity actual = StockEntity.buildFrom(stockDto);

        // then
        assertThat(actual.getId()).isEqualTo(stockDto.id());
        assertThat(actual.getQuantity()).isEqualTo(stockDto.quantity());
    }

    @DisplayName("StockEntity를 StockDto로 변환한다.")
    @Test
    void toStockDto() {
        // given
        StockEntity stockEntity = StockEntity.builder().id(1L).quantity(5000L).build();

        // when
        StockDto actual = stockEntity.toStockDto();

        // then
        assertThat(actual.id()).isEqualTo(stockEntity.getId());
        assertThat(actual.quantity()).isEqualTo(stockEntity.getQuantity());
    }

    @DisplayName("StockEntity에 ItemEntity 연관관계를 설정한다.")
    @Test
    void bind() {
        // given
        StockEntity stockEntity = StockEntity.builder().id(1L).build();
        ItemEntity itemEntity = ItemEntity.builder().id(1L).build();

        // when
        stockEntity.bindTo(itemEntity);

        // then
        assertThat(stockEntity.getItem()).isEqualTo(itemEntity);
    }
}