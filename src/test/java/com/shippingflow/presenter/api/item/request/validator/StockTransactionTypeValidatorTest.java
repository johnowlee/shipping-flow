package com.shippingflow.presenter.api.item.request.validator;

import com.shippingflow.core.domain.aggregate.item.model.local.StockTransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StockTransactionTypeValidatorTest {
    
    @DisplayName("stockTransactionType이 유효하면 true를 반환한다.")
    @ParameterizedTest
    @MethodSource("getValidTypes")
    void isValid_shouldReturnTrueWhenTypeIsValid(String types) {
        // given
        StockTransactionTypeValidator stockTransactionTypeValidator = new StockTransactionTypeValidator();
        
        // when
        boolean actual = stockTransactionTypeValidator.isValid(types, null);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("stockTransactionType이 빈값, 공백, null이면 false를 반환한다.")
    @ParameterizedTest
    @MethodSource("getBlanks")
    void isValid_shouldReturnFalseWhenTypeIsBlank(String blanks) {
        // given
        StockTransactionTypeValidator stockTransactionTypeValidator = new StockTransactionTypeValidator();

        // when
        boolean actual = stockTransactionTypeValidator.isValid(blanks, null);

        // then
        assertThat(actual).isFalse();
    }
    
    @DisplayName("stockTransactionType이 ENUM의 값과 일치하지 않으면 false를 반환한다.")
    @Test
    void isValid_shouldReturnFalseWhenTypeIsNotEqualToEnumValue() {
        // given
        String invalidType = "INVALID_TYPE";
        StockTransactionTypeValidator stockTransactionTypeValidator = new StockTransactionTypeValidator();

        // when
        boolean actual = stockTransactionTypeValidator.isValid(invalidType, null);

        // then
        assertThat(actual).isFalse();

        for (StockTransactionType value : StockTransactionType.values()) {
            assertThat(value.name()).isNotEqualTo(invalidType);
        }
    }
   
    private static List<String> getValidTypes() {
        return Arrays.asList("INCREASE", "DECREASE", "increase", "decrease");
    }

    private static List<String> getBlanks() {
        return Arrays.asList("", " ", null);
    }
}