package com.shippingflow.core.usecase.aggregate.item;

import com.shippingflow.core.domain.aggregate.item.model.local.StockTransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UpdateStockUseCaseFactoryTest {

    @Mock
    IncreaseStockUseCase increaseStockUseCase;

    @Mock
    DecreaseStockUseCase decreaseStockUseCase;

    UpdateStockUseCaseFactory factory;

    @BeforeEach
    void setUp() {
        given(increaseStockUseCase.getTransactionType()).willReturn(StockTransactionType.INCREASE);
        given(decreaseStockUseCase.getTransactionType()).willReturn(StockTransactionType.DECREASE);

        factory = new UpdateStockUseCaseFactory(List.of(increaseStockUseCase, decreaseStockUseCase));
    }

    @DisplayName("INCREASE 타입일 경우 IncreaseStockUseCase가 반환된다.")
    @Test
    void getUseCaseBy_shouldReturnIncreaseStockUseCaseWhenStockTransactionTypeIsINCREASE() {
        // when
        UpdateStockUseCase actual = factory.getUseCaseBy(StockTransactionType.INCREASE);

        // then
        assertThat(actual).isSameAs(increaseStockUseCase);
    }

    @DisplayName("DECREASE 타입일 경우 DecreaseStockUseCase가 반환된다.")
    @Test
    void getUseCaseBy_shouldReturnDecreaseStockUseCaseWhenStockTransactionTypeIsDECREASE() {
        // when
        UpdateStockUseCase actual = factory.getUseCaseBy(StockTransactionType.DECREASE);

        // then
        assertThat(actual).isSameAs(decreaseStockUseCase);
    }
}