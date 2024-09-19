package com.shippingflow.core.aggregate.domain.item.component;

import com.shippingflow.core.exception.DomainException;
import com.shippingflow.core.exception.error.ItemError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ItemValidatorTest {

    @Mock
    ItemReader itemReader;

    @InjectMocks
    ItemValidator itemValidator;

    @DisplayName("중복된 상품명이 없으면 아무일도 일어나지 않는다.")
    @Test
    void validateItemNameDuplication() {
        // given
        String itemName = "itemA";
        given(itemReader.doesItemExistByName(itemName)).willReturn(false);

        // when & then
        itemValidator.validateItemNameDuplication(itemName);
    }

    @DisplayName("중복된 상품명이 있으면 예외가 발생한다.")
    @Test
    void validateItemNameDuplication_shouldThrowExceptionWhenDuplicateItemNameExists() {
        // given
        String itemName = "itemA";
        given(itemReader.doesItemExistByName(itemName)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> itemValidator.validateItemNameDuplication(itemName))
                .isInstanceOf(DomainException.class)
                .hasMessage(ItemError.ITEM_NAME_ALREADY_EXISTS.getMessage());
    }

}