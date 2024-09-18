package com.shippingflow.core.usecase.aggregate.item;

import com.shippingflow.core.aggregate.domain.item.local.Stock;
import com.shippingflow.core.aggregate.domain.item.local.StockTransactionType;
import com.shippingflow.core.aggregate.domain.item.repository.ItemReaderRepository;
import com.shippingflow.core.aggregate.domain.item.repository.ItemWriterRepository;
import com.shippingflow.core.aggregate.domain.item.root.Item;
import com.shippingflow.core.exception.DomainException;
import com.shippingflow.core.exception.error.ItemError;
import com.shippingflow.core.aggregate.vo.ItemVo;
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
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class DecreaseStockUseCaseTest {

    @Mock
    ItemReaderRepository itemReaderRepository;

    @Mock
    ItemWriterRepository itemWriterRepository;

    @Mock
    ClockManager clockManager;

    @InjectMocks
    DecreaseStockUseCase decreaseStockUseCase;

    @DisplayName("재고 수량을 감소시키고 내역을 남긴다.")
    @Test
    void execute_decreaseStockQuantityAndRecordTransaction() {
        // given
        long itemId = 1L;
        long decreaseQuantity = 50L;
        LocalDateTime transactionDateTime = LocalDateTime.of(2024, 9, 18, 23, 30);

        Item item = Item.of(itemId, "ItemA", 1000L, "this is ItemA");
        Stock stock = Stock.builder().id(1L).quantity(300L).build();
        item.bind(stock);

        given(clockManager.getNowDateTime()).willReturn(transactionDateTime);
        given(itemReaderRepository.findById(itemId)).willReturn(Optional.of(item));

        DecreaseStockUseCase.Input input = DecreaseStockUseCase.Input.of(itemId, decreaseQuantity);

        // when
        DecreaseStockUseCase.Output output = decreaseStockUseCase.execute(input);

        // then
        assertThat(output.getItem().stock().quantity()).isEqualTo(300L - 50L);
        assertThat(output.getItem().stock().transactions()).hasSize(1)
                .extracting("transactionType", "quantity", "transactionDateTime")
                .contains(
                        tuple(StockTransactionType.DECREASE, 50L, transactionDateTime)
                );

        then(itemWriterRepository).should(times(1)).update(any(ItemVo.class));
    }

    @DisplayName("재고 감소 시 보유 수량이 부족하면 예외가 발생한다.")
    @Test
    void execute_shouldThrowExceptionWhenStockIsLessThanDecrease() {
        // given
        long itemId = 1L;
        long decreaseQuantity = 50L;

        Item item = Item.of(itemId, "ItemA", 1000L, "this is ItemA");
        Stock stock = Stock.builder().id(1L).quantity(30L).build();
        item.bind(stock);

        given(itemReaderRepository.findById(itemId)).willReturn(Optional.of(item));

        DecreaseStockUseCase.Input input = DecreaseStockUseCase.Input.of(itemId, decreaseQuantity);

        // when & then
        assertThat(item.getStock().getQuantity()).isLessThan(decreaseQuantity);
        assertThatThrownBy(() -> decreaseStockUseCase.execute(input))
                .isInstanceOf(DomainException.class)
                .hasMessage(ItemError.STOCK_SHORTAGE.getMessage());
    }

    @DisplayName("상품 조회 시 상품이 존재하지 않으면 예외가 발생한다.")
    @Test
    void execute_shouldThrowExceptionWhenItemIsNotFound() {
        // given
        UpdateStockUseCase.Input input = UpdateStockUseCase.Input.of(1L, 50L);

        given(itemReaderRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> decreaseStockUseCase.execute(input))
                .isInstanceOf(DomainException.class)
                .hasMessage(ItemError.NOT_FOUND_ITEM.getMessage());
    }
}