package com.shippingflow.core.usecase.aggregate.item;

import com.shippingflow.core.aggregate.domain.item.component.ItemReader;
import com.shippingflow.core.aggregate.domain.item.component.ItemWriter;
import com.shippingflow.core.aggregate.domain.item.model.local.Stock;
import com.shippingflow.core.aggregate.domain.item.model.local.StockTransactionType;
import com.shippingflow.core.aggregate.domain.item.model.root.Item;
import com.shippingflow.core.exception.DomainException;
import com.shippingflow.core.exception.error.ItemError;
import com.shippingflow.core.usecase.common.ClockManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class IncreaseStockUseCaseTest {

    @Mock
    ItemReader itemReader;

    @Mock
    ItemWriter itemWriter;

    @Mock
    ClockManager clockManager;

    @InjectMocks
    IncreaseStockUseCase increaseStockUseCase;

    @DisplayName("재고 수량을 증가시키고 내역을 남긴다.")
    @Test
    void execute_increaseStockQuantityAndRecordTransaction() {
        // given
        Item item = Item.builder().id(1L).name("itemA").build();
        long itemId = 1L;
        long quantityToAdd = 50L;
        LocalDateTime transactionDateTime = LocalDateTime.now();
        UpdateStockUseCase.Input input = UpdateStockUseCase.Input.of(itemId, quantityToAdd);

        Item increasedItem = Item.builder()
                .id(item.getId())
                .name(item.getName())
                .build();
        Stock stock = Stock.builder()
                .id(1L)
                .quantity(quantityToAdd)
                .build();
        increasedItem.bind(stock);
        increasedItem.recordStockTransaction(StockTransactionType.INCREASE, quantityToAdd, transactionDateTime);

        given(itemReader.getItemWithStockById(itemId)).willReturn(item);
        given(clockManager.getNowDateTime()).willReturn(transactionDateTime);
        given(itemWriter.updateStock(increasedItem)).willReturn(increasedItem);

        // when
        UpdateStockUseCase.Output output = increaseStockUseCase.execute(input);

        // then
        assertThat(output.getItem()).isNotNull();
        assertThat(output.getStock().quantity()).isEqualTo(50L);

        then(itemReader).should(times(1)).getItemWithStockById(itemId);
        then(itemWriter).should(times(1)).updateStock(item);
    }

    @DisplayName("상품 조회시 존재하지 않으면 예외가 발생한다.")
    @Test
    void execute_shouldThrowExceptionWhenItemIsNotFound() {
        // given
        UpdateStockUseCase.Input input = UpdateStockUseCase.Input.of(1L, 50L);

        given(itemReader.getItemWithStockById(1L)).willThrow(DomainException.from(ItemError.NOT_FOUND_ITEM));

        // when & then
        assertThatThrownBy(() -> increaseStockUseCase.execute(input))
                        .isInstanceOf(DomainException.class)
                        .hasMessage(ItemError.NOT_FOUND_ITEM.getMessage());
    }
}