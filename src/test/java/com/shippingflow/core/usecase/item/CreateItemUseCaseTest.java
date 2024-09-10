package com.shippingflow.core.usecase.item;

import com.shippingflow.core.domain.item.Item;
import com.shippingflow.core.domain.item.component.ItemValidator;
import com.shippingflow.core.domain.item.repository.ItemWriterRepository;
import com.shippingflow.core.domain.stock.Stock;
import com.shippingflow.core.domain.stock.repository.StockWriterRepository;
import com.shippingflow.core.exception.DomainException;
import com.shippingflow.core.exception.error.ItemError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CreateItemUseCaseTest {

    @Mock
    ItemWriterRepository itemWriterRepository;

    @Mock
    ItemValidator itemValidator;

    @Mock
    StockWriterRepository stockWriterRepository;

    @InjectMocks
    CreateItemUseCase createItemUseCase;

    @DisplayName("신규 상품을 등록한다.")
    @Test
    void execute() {
        // given
        String name = "상품A";
        Long price = 10_000L;
        String description = "상품A 입니다.";
        CreateItemUseCase.Input input = new CreateItemUseCase.Input(name, price, description);
        Item savedItem = Item.builder()
                .id(1L)
                .name(input.getName())
                .price(input.getPrice())
                .description(input.getDescription())
                .build();

        Stock stock = Stock.builder()
                .id(1L)
                .quantity(0)
                .build();
        stock.assignedTo(savedItem);


        willDoNothing().given(itemValidator).validateItemNameDuplication(name);
        given(itemWriterRepository.save(any(Item.class))).willReturn(savedItem);
        given(stockWriterRepository.save(any(Stock.class))).willReturn(stock);

        // when
        CreateItemUseCase.Output output = createItemUseCase.execute(input);
        
        // then
        Item actual = output.getItem();
        assertThat(actual.getId()).isEqualTo(1L);
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getDescription()).isEqualTo(description);
        assertThat(actual.getPrice()).isEqualTo(price);
        assertThat(actual.getStock()).isEqualTo(stock);
        then(stockWriterRepository).should(times(1)).save(any(Stock.class));
    }

    @DisplayName("신규 상품을 등록할때 중복된 상품 이름이 있으면 예외가 발생한다.")
    @Test
    void execute_shouldThrowExceptionWhenDuplicateItemNameExists() {
        // given
        String name = "상품A";
        Long price = 10_000L;
        String description = "상품A 입니다.";
        CreateItemUseCase.Input input = new CreateItemUseCase.Input(name, price, description);

        willThrow(DomainException.from(ItemError.ITEM_NAME_ALREADY_EXISTS))
                .given(itemValidator)
                .validateItemNameDuplication(name);

        // when & then
        assertThatThrownBy(() -> createItemUseCase.execute(input))
                        .isInstanceOf(DomainException.class)
                        .hasMessage(ItemError.ITEM_NAME_ALREADY_EXISTS.getMessage());
    }
}