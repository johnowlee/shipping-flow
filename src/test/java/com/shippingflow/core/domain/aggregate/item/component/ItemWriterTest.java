package com.shippingflow.core.domain.aggregate.item.component;

import com.shippingflow.core.domain.aggregate.item.dto.ItemSaveDto;
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
    void saveNewItem() {
        // given
        Item item = buildItemForTest(1L, "itemA", 1000L);

        given(itemWriterRepository.saveNewItem(any(ItemSaveDto.class))).willReturn(item.toItemWithStockDto());

        // when
        Item actual = itemWriter.saveNewItem(item);

        // then
        assertThat(actual.getId()).isEqualTo(1L);
        assertThat(actual.getName()).isEqualTo("itemA");
        assertThat(actual.getPrice()).isEqualTo(1000L);
    }

    @DisplayName("상품과 재고를 함께 저장한다.")
    @Test
    void saveWithStockAndIncreaseTransaction() {
        // given
        Item item = buildItemForTest(1L, "itemA", 1000L);
        Stock stock = buildStockForTest(1L, 5000L);
        StockTransaction stockTransaction = buildStockTransactionForTest(1L, 500L, StockTransactionType.INCREASE, LocalDateTime.now());
        stock.addTransaction(stockTransaction);
        item.bind(stock);

        given(itemWriterRepository.saveNewItem(any(ItemSaveDto.class))).willReturn(item.toItemWithStockDto());

        // when
        Item actual = itemWriter.saveNewItem(item);

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
        Item item = buildItemForTest(1L, "itemA", 1000L);
        Stock stock = buildStockForTest(1L, 1000L);
        StockTransaction stockTransaction = buildStockTransactionForTest(1L, 500L, StockTransactionType.INCREASE, LocalDateTime.now());
        stock.addTransaction(stockTransaction);
        item.bind(stock);

        given(itemWriterRepository.updateStock(any(ItemSaveDto.class))).willReturn(item.toItemWithStockDto());

        // when
        Item actual = itemWriter.updateStock(item);

        // then
        assertThat(actual.getId()).isEqualTo(1L);
        assertThat(actual.getName()).isEqualTo("itemA");
        assertThat(actual.getPrice()).isEqualTo(1000L);
        assertThat(actual.getStock().getId()).isEqualTo(1L);
        assertThat(actual.getStock().getQuantity()).isEqualTo(1000L);
    }

    private static Item buildItemForTest(long id, String name, long price) {
        return Item.builder()
                .id(id)
                .name(name)
                .price(price)
                .build();
    }

    private static Stock buildStockForTest(long id, long quantity) {
        return Stock.builder()
                .id(id)
                .quantity(quantity)
                .build();
    }

    private static StockTransaction buildStockTransactionForTest(long id, long quantity, StockTransactionType stockTransactionType, LocalDateTime now) {
        return StockTransaction.builder()
                .id(id)
                .quantity(quantity)
                .transactionType(stockTransactionType)
                .transactionDateTime(now)
                .build();
    }
}