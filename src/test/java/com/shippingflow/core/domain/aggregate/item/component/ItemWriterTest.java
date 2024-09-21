package com.shippingflow.core.domain.aggregate.item.component;

import com.shippingflow.core.domain.aggregate.item.dto.ItemAggregateDto;
import com.shippingflow.core.domain.aggregate.item.model.local.Stock;
import com.shippingflow.core.domain.aggregate.item.model.local.StockTransaction;
import com.shippingflow.core.domain.aggregate.item.model.local.StockTransactionType;
import com.shippingflow.core.domain.aggregate.item.repository.ItemWriterRepository;
import com.shippingflow.core.domain.aggregate.item.model.root.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ItemWriterTest {

    @Mock
    ItemWriterRepository itemWriterRepository;

    @InjectMocks
    ItemWriter itemWriter;

    @DisplayName("상품을 저장한다.")
    @Test
    void save() {
        // given
        Item item = Item.builder()
                .id(1L)
                .name("itemA")
                .price(1000L)
                .build();

        given(itemWriterRepository.save(any(ItemAggregateDto.class))).willReturn(item.toItemWithStockDto());

        // when
        Item actual = itemWriter.save(item);

        // then
        assertThat(actual.getId()).isEqualTo(1L);
        assertThat(actual.getName()).isEqualTo("itemA");
        assertThat(actual.getPrice()).isEqualTo(1000L);
    }

    @DisplayName("상품과 재고를 함께 저장한다.")
    @Test
    void saveWithStockAndIncreaseTransaction() {
        // given
        Item item = Item.builder()
                .id(1L)
                .name("itemA")
                .price(1000L)
                .build();

        Stock stock = Stock.builder()
                .id(1L)
                .quantity(5000L)
                .build();
        item.bind(stock);

        given(itemWriterRepository.save(any(ItemAggregateDto.class))).willReturn(item.toItemWithStockDto());

        // when
        Item actual = itemWriter.save(item);

        // then
        assertThat(actual.getId()).isEqualTo(1L);
        assertThat(actual.getName()).isEqualTo("itemA");
        assertThat(actual.getPrice()).isEqualTo(1000L);
        assertThat(actual.getStock().getId()).isEqualTo(1L);
        assertThat(actual.getStock().getQuantity()).isEqualTo(5000L);
    }

    @DisplayName("상품의 재고를 수정한다.")
    @Test
    void updateStock() {
        // given
        Item item = Item.builder()
                .id(1L)
                .name("itemA")
                .price(1000L)
                .build();
        Stock stock = Stock.builder()
                .id(1L)
                .quantity(1000L)
                .build();

        StockTransaction stockTransaction = StockTransaction.builder()
                .id(1L)
                .quantity(500L)
                .transactionType(StockTransactionType.INCREASE)
                .transactionDateTime(LocalDateTime.now())
                .build();
        stock.addTransaction(stockTransaction);
        item.bind(stock);

        given(itemWriterRepository.updateStock(any(ItemAggregateDto.class))).willReturn(item.toItemWithStockDto());

        // when
        Item actual = itemWriter.updateStock(item);

        // then
        assertThat(actual.getId()).isEqualTo(1L);
        assertThat(actual.getName()).isEqualTo("itemA");
        assertThat(actual.getPrice()).isEqualTo(1000L);
        assertThat(actual.getStock().getId()).isEqualTo(1L);
        assertThat(actual.getStock().getQuantity()).isEqualTo(1000L);
    }
}