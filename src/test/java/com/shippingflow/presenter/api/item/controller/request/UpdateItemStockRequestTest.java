package com.shippingflow.presenter.api.item.controller.request;

import com.shippingflow.core.domain.aggregate.item.model.local.StockTransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UpdateItemStockRequestTest {

    @DisplayName("transactionType 문자를 StockTransactionType Enum으로 변환한다.")
    @ParameterizedTest
    @MethodSource("getValidTypes")
    void convertStockTransactionTypeToEnum(String types) {
        // given
        UpdateItemStockRequest updateItemStockRequest = new UpdateItemStockRequest(types, 0);

        // when
        StockTransactionType actual = updateItemStockRequest.convertStockTransactionTypeToEnum();

        // then
        assertThat(actual).isNotNull();
    }

    @DisplayName("유효하지 않은 transactionType 문자를 StockTransactionType Enum으로 변환하면 예외가 발생한다.")
    @Test
    void convertStockTransactionTypeToEnum_shouldThrowExceptionWhenTypeIsNotValid() {
        // given
        String invalidType = "INVALID_TYPE";
        UpdateItemStockRequest updateItemStockRequest = new UpdateItemStockRequest(invalidType, 0);

        // when & then
        assertThatThrownBy(() -> updateItemStockRequest.convertStockTransactionTypeToEnum())
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static List<String> getValidTypes() {
        return Arrays.asList("INCREASE", "DECREASE", "increase", "decrease");
    }
}