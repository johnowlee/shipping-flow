package com.shippingflow.core.usecase.aggregate.item;

import com.shippingflow.core.aggregate.domain.item.component.ItemValidator;
import com.shippingflow.core.aggregate.domain.item.component.ItemWriter;
import com.shippingflow.core.aggregate.domain.item.dto.ItemWithStockDto;
import com.shippingflow.core.aggregate.domain.item.model.local.Stock;
import com.shippingflow.core.aggregate.domain.item.model.local.StockTransaction;
import com.shippingflow.core.aggregate.domain.item.model.local.StockTransactionType;
import com.shippingflow.core.aggregate.domain.item.model.root.Item;
import com.shippingflow.core.exception.DomainException;
import com.shippingflow.core.exception.error.ItemError;
import com.shippingflow.core.usecase.common.ClockManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CreateItemUseCaseTest {

    @Mock
    ItemWriter itemWriter;

    @Mock
    ItemValidator itemValidator;

    @Mock
    ClockManager clockManager;

    @InjectMocks
    CreateItemUseCase createItemUseCase;

    @DisplayName("신규 상품을 등록한다.")
    @Test
    void execute() {
        // given
        String name = "상품A";
        Long price = 10_000L;
        String description = "상품A 입니다.";
        long quantity = 1000L;
        LocalDateTime transactionDateTime = LocalDateTime.of(2024, 9, 16, 16, 0, 0);
        CreateItemUseCase.Input input = CreateItemUseCase.Input.of(name, price, description, quantity);

        given(clockManager.getNowDateTime()).willReturn(transactionDateTime);
        Item createdItem = Item.create(input.getName(), input.getPrice(), input.getDescription());
        Stock createdStock = Stock.create(quantity);
        createdItem.bind(createdStock);
        createdItem.recordStockTransaction(StockTransactionType.INCREASE, quantity, transactionDateTime);

        Item savedItem = Item.builder()
                .id(1L)
                .name(name)
                .price(price)
                .description(description)
                .build();

        Stock savedStock = Stock.builder()
                .id(1L)
                .quantity(quantity)
                .build();

        StockTransaction savedStockTransaction = StockTransaction.builder()
                .id(1L)
                .transactionType(StockTransactionType.INCREASE)
                .quantity(quantity)
                .transactionDateTime(transactionDateTime)
                .build();

        savedStock.addTransaction(savedStockTransaction);
        savedItem.bind(savedStock);

        willDoNothing().given(itemValidator).validateItemNameDuplication(name);
        given(itemWriter.save(createdItem)).willReturn(savedItem);

        // when
        CreateItemUseCase.Output output = createItemUseCase.execute(input);

        // then
        ItemWithStockDto itemWithStockDto = output.getItemWithStockDto();
        assertThat(itemWithStockDto.item().id()).isEqualTo(savedItem.getId());
        assertThat(itemWithStockDto.item().name()).isEqualTo(savedItem.getName());
        assertThat(itemWithStockDto.item().description()).isEqualTo(savedItem.getDescription());
        assertThat(itemWithStockDto.item().price()).isEqualTo(savedItem.getPrice());
        assertThat(itemWithStockDto.stock()).isEqualTo(savedStock.toDto());
        assertThat(itemWithStockDto.stock().quantity()).isEqualTo(quantity);
    }

    @DisplayName("신규 상품을 등록할때 중복된 상품 이름이 있으면 예외가 발생한다.")
    @Test
    void execute_shouldThrowExceptionWhenDuplicateItemNameExists() {
        // given
        String name = "상품A";
        Long price = 10_000L;
        String description = "상품A 입니다.";
        CreateItemUseCase.Input input = CreateItemUseCase.Input.of(name, price, description, null);

        willThrow(DomainException.from(ItemError.ITEM_NAME_ALREADY_EXISTS))
                .given(itemValidator)
                .validateItemNameDuplication(name);

        // when & then
        assertThatThrownBy(() -> createItemUseCase.execute(input))
                        .isInstanceOf(DomainException.class)
                        .hasMessage(ItemError.ITEM_NAME_ALREADY_EXISTS.getMessage());
    }
}