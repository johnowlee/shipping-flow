package com.shippingflow.core.domain.aggregate.item.model.local;

import com.shippingflow.core.domain.aggregate.item.dto.StockDto;
import com.shippingflow.core.domain.aggregate.item.dto.StockTransactionDto;
import com.shippingflow.core.domain.aggregate.item.model.root.Item;
import com.shippingflow.core.exception.DomainException;
import com.shippingflow.core.exception.error.ItemError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.tuple;

class StockTest {

    @DisplayName("StockDto를 Stock으로 변환한다.")
    @Test
    void fromStockDto() {
        // given
        long stockId = 1L;
        long quantity = 1000L;
        StockDto stockDto = new StockDto(stockId, quantity);

        // when
        Stock actual = Stock.from(stockDto);

        // then
        assertThat(actual.getId()).isEqualTo(stockId);
        assertThat(actual.getQuantity()).isEqualTo(quantity);
    }
    
    @DisplayName("재고에 상품 연관관계를 설정한다.")
    @Test
    void bindTo() {
        // given
        Item item = Item.builder().id(1L).name("itemA").price(1000L).description("this is ItemA").build();
        Stock stock = buildStock(1L, 50L);
        
        // when
        stock.bindTo(item);
        
        // then
        assertThat(stock.getItem()).isEqualTo(item);
        assertThat(stock.getItem().getName()).isEqualTo(item.getName());
        assertThat(stock.getItem().getPrice()).isEqualTo(item.getPrice());
        assertThat(stock.getItem().getDescription()).isEqualTo(item.getDescription());
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
        assertThat(stock.getQuantity()).isEqualTo(10L + 30L + 60L);
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
        assertThat(stock.getQuantity()).isEqualTo(100L - 30L - 60L);
    }

    @DisplayName("재고 수량 감소 시 남아있는 재고의 양이 부족하면 예외가 발생한다.")
    @Test
    void decrease_shouldThrowExceptionWhenQuantityLessThanDecrease() {
        // given
        Stock stock = buildStock(1L, 100L);

        // when & then
        stock.decrease(100L);
        assertThatThrownBy(() -> stock.decrease(1L))
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
        assertThat(stock.getTransactions()).hasSize(1)
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
        assertThatThrownBy(() -> stock.recordTransaction(transactionType, quantity, transactionDateTime))
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
        assertThat(stock.getTransactions()).hasSize(1)
                .extracting("id", "transactionType", "quantity", "transactionDateTime")
                .contains(
                        tuple(transactionId, transactionType, quantity, transactionDateTime)
                );
    }

    @DisplayName("StockTransactionDto 리스트를 StockTransaction으로 변환 후 리스트에 추가한다.")
    @Test
    void addTransactionsFrom() {
        // given
        Stock stock = Stock.builder().id(1L).build();

        long transactionId = 1L;
        long quantity = 1000L;
        StockTransactionType transactionType = StockTransactionType.INCREASE;
        LocalDateTime transactionDateTime = LocalDateTime.of(2024, 9, 20, 23, 30);
        StockTransactionDto stockTransactionDto = new StockTransactionDto(transactionId, quantity, transactionType, transactionDateTime);

        // when
        stock.addTransactionsFrom(List.of(stockTransactionDto));

        // then
        assertThat(stock.getTransactions()).hasSize(1)
                .extracting("id", "transactionType", "quantity", "transactionDateTime")
                .contains(
                        tuple(transactionId, transactionType, quantity, transactionDateTime)
                );
    }

    @DisplayName("Stock을 StockDto로 변환한다.")
    @Test
    void toDto() {
        // given
        Stock stock = Stock.builder()
                .id(1L)
                .quantity(1000L)
                .build();

        // when
        StockDto actual = stock.toDto();

        // then
        assertThat(actual.id()).isEqualTo(1L);
        assertThat(actual.quantity()).isEqualTo(1000L);
    }

    @DisplayName("StockTransactions를 StockTransactionDtoList로 변환한다.")
    @Test
    void transactionsToDtoList() {
        // given
        Stock stock = Stock.builder().id(1L).build();

        long transactionId = 1L;
        long quantity = 1000L;
        StockTransactionType transactionType = StockTransactionType.INCREASE;
        LocalDateTime transactionDateTime = LocalDateTime.of(2024, 9, 20, 23, 30);
        StockTransaction stockTransaction = StockTransaction.of(transactionId, quantity, transactionType, transactionDateTime);

        stock.addTransaction(stockTransaction);

        // when
        List<StockTransactionDto> actual = stock.transactionsToDtoList();

        // then
        assertThat(actual).hasSize(1)
                .extracting("id", "transactionType", "quantity", "transactionDateTime")
                .contains(
                        tuple(transactionId, transactionType, quantity, transactionDateTime)
                );
    }

    private static Stock buildStock(long id, long quantity) {
        return Stock.builder().id(id).quantity(quantity).build();
    }
}