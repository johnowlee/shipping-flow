package com.shippingflow.core.aggregate.item.component;

import com.shippingflow.core.aggregate.item.repository.ItemReaderRepository;
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
    ItemReaderRepository itemReaderRepository;

    @InjectMocks
    ItemValidator itemValidator;

    @DisplayName("중복된 상품명이 없으면 아무일도 일어나지 않는다.")
    @Test
    void validateItemNameDuplication() {
        // given
        String itenName = "itemA";
        given(itemReaderRepository.existsByName(itenName)).willReturn(false);

        // when & then
        itemValidator.validateItemNameDuplication(itenName);
    }

    @DisplayName("중복된 상품명이 있으면 예외가 발생한다.")
    @Test
    void validateItemNameDuplication_shouldThrowExceptionWhenDuplicateItemNameExists() {
        // given
        String itenName = "itemA";
        given(itemReaderRepository.existsByName(itenName)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> itemValidator.validateItemNameDuplication(itenName))
                .isInstanceOf(DomainException.class)
                .hasMessage(ItemError.ITEM_NAME_ALREADY_EXISTS.getMessage());
    }

}