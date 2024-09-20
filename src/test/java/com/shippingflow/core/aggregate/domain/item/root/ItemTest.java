package com.shippingflow.core.aggregate.domain.item.root;

import com.shippingflow.core.aggregate.domain.item.local.Stock;
import com.shippingflow.core.aggregate.domain.item.local.StockTransactionType;
import com.shippingflow.core.aggregate.domain.item.dto.ItemDto;
import com.shippingflow.core.aggregate.domain.item.dto.ItemWithStockDto;
import com.shippingflow.core.aggregate.domain.item.dto.StockDto;
import com.shippingflow.core.exception.DomainException;
import com.shippingflow.core.exception.error.ItemError;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ItemTest {

    @DisplayName("신규 상품을 생성한다.")
    @Test
    void create() {
        // given
        String name = "itemA";
        Long price = 10_000L;
        String description = "this is itemA";

        // when
        Item actual = Item.create(name, price, description);

        // then
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getPrice()).isEqualTo(price);
        assertThat(actual.getDescription()).isEqualTo(description);
    }

    @DisplayName("상품에 재고 연관관계를 설정한다.")
    @Test
    void bind() {
        // given
        Item item = buildItem(1L, "itemA", 1000L, "this is ItemA");
        Stock stock = Stock.builder().id(1L).quantity(50L).build();

        // when
        item.bind(stock);

        // then
        assertThat(item.getStock()).isEqualTo(stock);
        assertThat(stock.getItem()).isEqualTo(item);
    }

    @DisplayName("상품의 재고를 증가 시킨다.")
    @Test
    void increaseStock() {
        // given
        Item item = buildItem(1L, "itemA", 1000L, "this is ItemA");
        Stock stock = mock(Stock.class);
        item.bind(stock);

        // when
        item.increaseStock(anyLong());
        item.increaseStock(anyLong());

        // then
        then(stock).should(times(2)).increase(anyLong());
    }

    @DisplayName("상품의 재고를 증가 시 재고 객체가 없으면 새로 생성하고 재고를 증가 시킨다.")
    @Test
    void increaseStock_shouldCreateStockAndIncreaseIfAbsent() {
        // given
        Item item = buildItem(1L, "itemA", 1000L, "this is ItemA");

        // when & then
        assertThat(item.getStock()).isNull();

        // and
        item.increaseStock(50L);

        // then
        assertThat(item.getStock()).isNotNull();
        assertThat(item.getStock().getQuantity()).isEqualTo(50L);
    }

    @DisplayName("상품의 재고를 감소 시킨다.")
    @Test
    void decreaseStock() {
        // given
        Item item = buildItem(1L, "itemA", 1000L, "this is ItemA");
        Stock stock = mock(Stock.class);
        item.bind(stock);

        // when
        item.decreaseStock(anyLong());

        // then
        then(stock).should(times(1)).decrease(anyLong());
    }

    @DisplayName("상품의 재고를 감소 시 재고 객체가 없으면 예외가 발생한다.")
    @Test
    void decreaseStock_shouldThrowExceptionWhenStockIsNull() {
        // given
        Item item = buildItem(1L, "itemA", 1000L, "this is ItemA");

        // when & then
        Assertions.assertThatThrownBy(() -> item.decreaseStock(500L))
                        .isInstanceOf(DomainException.class)
                        .hasMessage(ItemError.STOCK_SHORTAGE.getMessage());
    }

    @DisplayName("상품의 입출고 내역을 기록한다.")
    @Test
    void recordStockTransaction() {
        // given
        Item item = buildItem(1L, "itemA", 1000L, "this is ItemA");
        Stock stock = mock(Stock.class);
        item.bind(stock);
        StockTransactionType transactionType = StockTransactionType.INCREASE;
        long quantity = 100L;
        LocalDateTime transactionDateTime = LocalDateTime.of(2024, 9, 13, 16, 0, 0);

        // when
        item.recordStockTransaction(transactionType, quantity, transactionDateTime);

        // then
        then(stock).should(times(1)).recordTransaction(eq(transactionType), eq(quantity), eq(transactionDateTime));
    }

    @DisplayName("ItemDto를 Item으로 변환한다.")
    @Test
    void fromItemDto() {
        // given
        long id = 1L;
        String name = "itemA";
        long price = 1000L;
        String description = "description";
        ItemDto itemDto = new ItemDto(id, name, price, description);

        // when
        Item actual = Item.from(itemDto);

        // then
        assertThat(actual.getId()).isEqualTo(id);
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getPrice()).isEqualTo(price);
        assertThat(actual.getDescription()).isEqualTo(description);
    }

    @DisplayName("ItemWithStockDto를 Stock이 포함된 Item으로 변환한다.")
    @Test
    void fromItemWithStockDto_shouldReturnWithStockWhenStockIsNotNull() {
        // given
        long itemId = 1L;
        String name = "itemA";
        long price = 1000L;
        String description = "description";
        ItemDto itemDto = new ItemDto(itemId, name, price, description);

        long stockId = 1L;
        long quantity = 1000L;
        StockDto stockDto = new StockDto(stockId, quantity);

        ItemWithStockDto itemWithStockDto = new ItemWithStockDto(itemDto, stockDto);

        // when
        Item actual = Item.from(itemWithStockDto);

        // then
        assertThat(actual.getId()).isEqualTo(itemId);
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getPrice()).isEqualTo(price);
        assertThat(actual.getDescription()).isEqualTo(description);
        assertThat(actual.getStock().getId()).isEqualTo(stockId);
        assertThat(actual.getStock().getQuantity()).isEqualTo(quantity);
    }

    @DisplayName("ItemWithStockDto에서 StockDto가 null이면 Item만 변환한다.")
    @Test
    void fromItemWithStockDto_shouldReturnWithoutStockWhenStockIsNull() {
        // given
        long itemId = 1L;
        String name = "itemA";
        long price = 1000L;
        String description = "description";
        ItemDto itemDto = new ItemDto(itemId, name, price, description);

        StockDto stockDto = null;

        ItemWithStockDto itemWithStockDto = new ItemWithStockDto(itemDto, stockDto);

        // when
        Item actual = Item.from(itemWithStockDto);

        // then
        assertThat(actual.getId()).isEqualTo(itemId);
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getPrice()).isEqualTo(price);
        assertThat(actual.getDescription()).isEqualTo(description);
        assertThat(actual.getStock()).isNull();
    }

    private Item buildItem(Long id, String name, Long price, String description) {
        return Item.builder().id(id).name(name).price(price).description(description).build();
    }

}