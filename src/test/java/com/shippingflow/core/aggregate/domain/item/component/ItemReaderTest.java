package com.shippingflow.core.aggregate.domain.item.component;

import com.shippingflow.core.aggregate.domain.item.repository.ItemReaderRepository;
import com.shippingflow.core.aggregate.domain.item.repository.dto.ItemDto;
import com.shippingflow.core.aggregate.domain.item.repository.dto.ItemWithStockDto;
import com.shippingflow.core.aggregate.domain.item.repository.dto.StockDto;
import com.shippingflow.core.aggregate.domain.item.root.Item;
import com.shippingflow.core.exception.DomainException;
import com.shippingflow.core.exception.error.ItemError;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ItemReaderTest {

    @Mock
    ItemReaderRepository itemReaderRepository;

    @InjectMocks
    ItemReader itemReader;

    @DisplayName("상품명으로 상품이 존재하면 true를 반환한다.")
    @Test
    void doseItemExistByName_shouldReturnTrueWhenItemExists() {
        // given
        given(itemReaderRepository.existsByName("itemA")).willReturn(true);

        // when
        boolean actual = itemReader.doesItemExistByName("itemA");

        // then
        assertThat(actual).isTrue();
        then(itemReaderRepository).should(times(1)).existsByName(eq("itemA"));
    }

    @DisplayName("상품명으로 상품이 존재하지 않으면 false를 반환한다.")
    @Test
    void doseItemExistByName_shouldReturnFalseWhenItemDoesNotExist() {
        // given
        given(itemReaderRepository.existsByName("itemA")).willReturn(false);

        // when
        boolean actual = itemReader.doesItemExistByName("itemA");

        // then
        assertThat(actual).isFalse();
        then(itemReaderRepository).should(times(1)).existsByName(eq("itemA"));
    }

    @DisplayName("상품 ID로 상품을 조회한다.")
    @Test
    void findItemById() {
        // given
        long itemId = 1L;
        Item item = Item.builder()
                .id(itemId)
                .name("itemA")
                .description("this is itemA")
                .build();
        given(itemReaderRepository.findById(itemId)).willReturn(Optional.of(item));

        // when
        Item actual = itemReader.findItemById(itemId);

        // then
        assertThat(actual.getId()).isEqualTo(item.getId());
        assertThat(actual.getName()).isEqualTo(item.getName());
        assertThat(actual.getDescription()).isEqualTo(item.getDescription());
        then(itemReaderRepository).should(times(1)).findById(eq(itemId));
    }

    @DisplayName("상품 ID로 조회 시 상품이 존재하지 않으면 예외가 발생한다.")
    @Test
    void findItemById_shouldThrowExceptionWhenItemDoesNotExist() {
        // given
        given(itemReaderRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> itemReader.findItemById(1L))
                        .isInstanceOf(DomainException.class)
                        .hasMessage(ItemError.NOT_FOUND_ITEM.getMessage());
        then(itemReaderRepository).should(times(1)).findById(eq(1L));
    }

    @DisplayName("상품 ID로 상품과 재고를 조회한다.")
    @Test
    void getItemWithStockById() {
        // given
        long itemId = 1L;
        String name = "itemA";
        long price = 1000L;
        String description = "description";
        ItemDto itemDto = new ItemDto(itemId, name, price, description);

        long stockId = 1L;
        long quantity = 1000L;
        StockDto stockDto = new StockDto(stockId, quantity);

        ItemWithStockDto itemWithStockDto = new ItemWithStockDto(itemDto, stockDto);
        given(itemReaderRepository.findItemWithStockById(itemId)).willReturn(Optional.of(itemWithStockDto));

        // when
        Item actual = itemReader.getItemWithStockById(itemId);

        // then
        assertThat(actual.getId()).isEqualTo(itemId);
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getPrice()).isEqualTo(price);
        assertThat(actual.getDescription()).isEqualTo(description);
        assertThat(actual.getStock().getId()).isEqualTo(stockId);
        assertThat(actual.getStock().getQuantity()).isEqualTo(quantity);
        then(itemReaderRepository).should(times(1)).findItemWithStockById(eq(itemId));
    }

    @DisplayName("상품 ID로 조회 시 상품이 존재하지 않으면 예외가 발생한다.")
    @Test
    void getItemWithStockById_shouldThrowExceptionWhenItemDoesNotExist() {
        // given
        given(itemReaderRepository.findItemWithStockById(anyLong())).willReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> itemReader.getItemWithStockById(1L))
                .isInstanceOf(DomainException.class)
                .hasMessage(ItemError.NOT_FOUND_ITEM.getMessage());
        then(itemReaderRepository).should(times(1)).findById(eq(1L));
    }
}