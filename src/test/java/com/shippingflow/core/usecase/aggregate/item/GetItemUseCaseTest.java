package com.shippingflow.core.usecase.aggregate.item;

import com.shippingflow.core.domain.aggregate.item.component.ItemReader;
import com.shippingflow.core.domain.aggregate.item.dto.ItemDto;
import com.shippingflow.core.domain.aggregate.item.dto.StockDto;
import com.shippingflow.core.domain.aggregate.item.model.local.Stock;
import com.shippingflow.core.domain.aggregate.item.model.root.Item;
import com.shippingflow.core.exception.DomainException;
import com.shippingflow.core.exception.error.ItemError;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GetItemUseCaseTest {

    @Mock
    ItemReader itemReader;

    @InjectMocks
    GetItemUseCase getItemUseCase;

    @DisplayName("상품 ID로 상품과 재고정보를 조회한다.")
    @Test
    void execute() {
        // given
        long itemId = 1L;
        String name = "itemA";
        long price = 10_000L;
        String description = "this is ItemA";
        Item item = Item.builder()
                .id(itemId)
                .name(name)
                .price(price)
                .description(description)
                .build();

        long stockId = 1L;
        long quantity = 1000L;
        Stock stock = Stock.builder()
                .id(stockId)
                .quantity(quantity)
                .build();
        item.bind(stock);

        given(itemReader.getItemWithStockById(itemId)).willReturn(item);

        GetItemUseCase.Input input = GetItemUseCase.Input.of(itemId);

        // when
        GetItemUseCase.Output actual = getItemUseCase.execute(input);

        // then
        ItemDto itemDto = actual.getItemWithStockDto().item();
        assertThat(itemDto.id()).isEqualTo(itemId);
        assertThat(itemDto.name()).isEqualTo(name);
        assertThat(itemDto.price()).isEqualTo(price);
        assertThat(itemDto.description()).isEqualTo(description);

        StockDto stockDto = actual.getItemWithStockDto().stock();
        assertThat(stockDto.id()).isEqualTo(stockId);
        assertThat(stockDto.quantity()).isEqualTo(quantity);
    }

    @DisplayName("등록되지 않은 상품 조회 시 예외가 발생한다.")
    @Test
    void execute_shouldThrowExceptionWhenItemIsNotFound() {
        // given
        given(itemReader.getItemWithStockById(anyLong())).willThrow(DomainException.from(ItemError.NOT_FOUND_ITEM));

        GetItemUseCase.Input input = GetItemUseCase.Input.of(1L);

        // when & then
        Assertions.assertThatThrownBy(() -> getItemUseCase.execute(input))
                .isInstanceOf(DomainException.class)
                .hasMessage(ItemError.NOT_FOUND_ITEM.getMessage());
    }
}
