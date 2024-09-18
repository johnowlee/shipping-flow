package com.shippingflow.core.usecase.item;

import com.shippingflow.core.aggregate.domain.item.component.ItemValidator;
import com.shippingflow.core.aggregate.domain.item.local.Stock;
import com.shippingflow.core.aggregate.domain.item.local.StockTransaction;
import com.shippingflow.core.aggregate.domain.item.local.StockTransactionType;
import com.shippingflow.core.aggregate.domain.item.repository.ItemWriterRepository;
import com.shippingflow.core.aggregate.domain.item.root.Item;
import com.shippingflow.core.exception.DomainException;
import com.shippingflow.core.exception.error.ItemError;
import com.shippingflow.core.usecase.aggregate.item.CreateItemUseCase;
import com.shippingflow.core.aggregate.vo.ItemVo;
import com.shippingflow.core.usecase.common.ClockManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CreateItemUseCaseTest {

    @Mock
    ItemWriterRepository itemWriterRepository;

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

        ItemVo createdItemVo = createdItem.toVo();

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
        given(itemWriterRepository.save(createdItemVo)).willReturn(savedItem);

        // when
        CreateItemUseCase.Output output = createItemUseCase.execute(input);

        // then
        ItemVo savedItemVo = output.getItem();
        assertThat(savedItemVo.id()).isEqualTo(savedItem.getId());
        assertThat(savedItemVo.name()).isEqualTo(savedItem.getName());
        assertThat(savedItemVo.description()).isEqualTo(savedItem.getDescription());
        assertThat(savedItemVo.price()).isEqualTo(savedItem.getPrice());
        assertThat(savedItemVo.stock()).isEqualTo(savedStock.toVo());
        assertThat(savedItemVo.stock().quantity()).isEqualTo(quantity);
        assertThat(savedItemVo.stock().transactions()).hasSize(1)
                .extracting("quantity", "transactionType", "transactionDateTime")
                .contains(
                        tuple(1000L, StockTransactionType.INCREASE, transactionDateTime)
                );
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