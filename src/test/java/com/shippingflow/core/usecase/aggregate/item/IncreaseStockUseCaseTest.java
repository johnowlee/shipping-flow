package com.shippingflow.core.usecase.aggregate.item;

import com.shippingflow.core.domain.aggregate.item.local.Stock;
import com.shippingflow.core.domain.aggregate.item.local.StockTransactionType;
import com.shippingflow.core.domain.aggregate.item.repository.ItemReaderRepository;
import com.shippingflow.core.domain.aggregate.item.repository.ItemWriterRepository;
import com.shippingflow.core.domain.aggregate.item.root.Item;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.tuple;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class IncreaseStockUseCaseTest {

    @Mock
    ItemReaderRepository itemReaderRepository;

    @Mock
    ItemWriterRepository itemWriterRepository;

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
                .quantity(quantityToAdd)
                .build();
        increasedItem.bind(stock);
        increasedItem.recordStockTransaction(StockTransactionType.INCREASE, quantityToAdd, transactionDateTime);

        given(itemReaderRepository.findById(itemId)).willReturn(Optional.of(item));
        given(clockManager.getNowDateTime()).willReturn(transactionDateTime);
        given(itemWriterRepository.update(increasedItem.toVo())).willReturn(increasedItem);

        // when
        UpdateStockUseCase.Output output = increaseStockUseCase.execute(input);

        // then
        assertThat(output.getItem()).isNotNull();
        assertThat(output.getItem().stock().quantity()).isEqualTo(50L);
        assertThat(output.getItem().stock().transactions()).hasSize(1)
                .extracting("transactionType", "quantity", "transactionDateTime")
                .contains(
                        tuple(StockTransactionType.INCREASE, quantityToAdd, transactionDateTime)
                );
        then(itemReaderRepository).should(times(1)).findById(itemId);
        then(itemWriterRepository).should(times(1)).update(item.toVo());
    }

    @DisplayName("상품 조회시 존재하지 않으면 예외가 발생한다.")
    @Test
    void execute_shouldThrowExceptionWhenItemIsNotFound() {
        // given
        UpdateStockUseCase.Input input = UpdateStockUseCase.Input.of(1L, 50L);

        given(itemReaderRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> increaseStockUseCase.execute(input))
                        .isInstanceOf(DomainException.class)
                        .hasMessage(ItemError.NOT_FOUND_ITEM.getMessage());
    }
}