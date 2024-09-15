package com.shippingflow.core.usecase.aggregate.item;

import com.shippingflow.core.domain.aggregate.item.local.Stock;
import com.shippingflow.core.domain.aggregate.item.repository.ItemReaderRepository;
import com.shippingflow.core.domain.aggregate.item.repository.ItemWriterRepository;
import com.shippingflow.core.domain.aggregate.item.root.Item;
import com.shippingflow.core.usecase.aggregate.item.vo.ItemVo;
import com.shippingflow.core.usecase.common.ClockManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class IncreaseStockUseCaseTest {

    @Mock
    ItemReaderRepository itemReaderRepository;

    @Mock
    ItemWriterRepository itemWriterRepository;

    @Mock
    ClockManager clockManager;

    @InjectMocks
    IncreaseStockUseCase increaseStockUseCase;

    @DisplayName("재고 수량을 증가시키고 내역을 남긴다.")
    @Test
    void execute_increaseStockQuantityAndRecordTransaction() {
        // given
        long itemId = 1L;
        long quantity = 1000L;
        Item item = Item.builder().id(itemId).build();
        Stock stock = Stock.builder().id(1L).build();
        item.bind(stock);

        UpdateStockUseCase.Input input = UpdateStockUseCase.Input.of(1L, quantity);

        given(itemReaderRepository.findById(input.getItemId())).willReturn(Optional.of(item));
        given(itemWriterRepository.update(any(ItemVo.class))).willReturn(item);

        // when
        UpdateStockUseCase.Output result = increaseStockUseCase.execute(input);

        // then
        ItemVo actual = result.getItem();
        assertThat(actual.id()).isEqualTo(itemId);
        assertThat(actual.stock().quantity()).isEqualTo(quantity);
    }

}