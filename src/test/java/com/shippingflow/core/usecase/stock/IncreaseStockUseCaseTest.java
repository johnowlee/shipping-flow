package com.shippingflow.core.usecase.stock;

import com.shippingflow.core.domain.stock.Stock;
import com.shippingflow.core.domain.stock.StockTransaction;
import com.shippingflow.core.domain.stock.StockTransactionType;
import com.shippingflow.core.domain.stock.repository.StockReaderRepository;
import com.shippingflow.core.domain.stock.repository.StockTransactionWriterRepository;
import com.shippingflow.core.domain.stock.repository.StockWriterRepository;
import com.shippingflow.core.exception.DomainException;
import com.shippingflow.core.exception.error.StockError;
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
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class IncreaseStockUseCaseTest {

    @Mock
    StockReaderRepository stockReaderRepository;

    @Mock
    StockWriterRepository stockWriterRepository;

    @Mock
    StockTransactionWriterRepository stockTransactionWriterRepository;

    @Mock
    ClockManager clockManager;

    @InjectMocks
    IncreaseStockUseCase increaseStockUseCase;

    @DisplayName("재고 수량을 증가시키고 내역을 남긴다.")
    @Test
    void execute_increaseStockQuantityAndSaveTransaction() {
        // given
        long id = 1L;
        long quantity = 100L;
        Stock stock = Stock.builder().id(id).quantity(quantity).build();
        LocalDateTime transactionDateTime = LocalDateTime.of(2024, 9, 10, 18, 0, 0);
        StockTransaction stockTransaction = StockTransaction.builder()
                .id(1L)
                .stock(stock)
                .quantity(quantity)
                .transactionType(StockTransactionType.INCREASE)
                .transactionDateTime(transactionDateTime)
                .build();
        given(stockReaderRepository.findById(id)).willReturn(Optional.of(stock));
        given(stockWriterRepository.update(stock)).willReturn(stock);
        given(clockManager.getNowDateTime()).willReturn(transactionDateTime);
        given(stockTransactionWriterRepository.save(any(StockTransaction.class))).willReturn(stockTransaction);

        // when
        UpdateStockUseCase.Input input = UpdateStockUseCase.Input.of(id, 300L);
        UpdateStockUseCase.Output output = increaseStockUseCase.execute(input);

        // then
        Stock actual = output.getStock();
        assertThat(actual.getId()).isEqualTo(id);
        assertThat(actual.getQuantity()).isEqualTo(100L + 300L);

        then(stockTransactionWriterRepository).should(times(1)).save(any(StockTransaction.class));
        assertThat(actual.getTransactions()).hasSize(1)
                .extracting("id", "stock", "quantity", "transactionType", "transactionDateTime")
                .contains(tuple(1L, stock, quantity, StockTransactionType.INCREASE, transactionDateTime));
    }

    @DisplayName("재고 수량을 증가시킬 때 재고를 찾을 수 없으면 예외가 발생한다.")
    @Test
    void execute_shouldThrowExceptionWhenStockIsNotFound() {
        // given
        long id = 1L;
        given(stockReaderRepository.findById(id)).willReturn(Optional.empty());

        // when & then
        UpdateStockUseCase.Input input = UpdateStockUseCase.Input.of(id, 300L);
        assertThatThrownBy(() -> increaseStockUseCase.execute(input))
                        .isInstanceOf(DomainException.class)
                        .hasMessage(StockError.NOT_FOUND_STOCK.getMessage());
    }
}