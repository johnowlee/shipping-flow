package com.shippingflow.core.usecase.aggregate.item;

import com.shippingflow.core.domain.aggregate.item.component.ItemReader;
import com.shippingflow.core.domain.aggregate.item.dto.ItemDto;
import com.shippingflow.core.domain.aggregate.item.dto.ItemWithStockDto;
import com.shippingflow.core.domain.aggregate.item.dto.StockDto;
import com.shippingflow.core.domain.common.pagination.PageResponse;
import com.shippingflow.core.domain.common.pagination.PaginationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetItemsUseCaseTest {

    @Mock
    private ItemReader itemReader;

    @InjectMocks
    private GetItemsUseCase getItemsUseCase;

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void execute() {
        // given
        ItemDto itemDto = new ItemDto(1L, "ItemA", 1000L, "this is ItemA");
        StockDto stockDto = new StockDto(1L, 100L);
        ItemWithStockDto itemWithStockDto = ItemWithStockDto.of(itemDto, stockDto);
        List<ItemWithStockDto> itemWithStockDtoList = List.of(itemWithStockDto);


        PageResponse<ItemWithStockDto> pageResponse = new PageResponse<>(itemWithStockDtoList, 0, 10, 1, 1);

        PaginationRequest paginationRequest = PaginationRequest.of(1, 10, "name", "asc");
        given(itemReader.getItems(paginationRequest)).willReturn(pageResponse);

        GetItemsUseCase.Input input = GetItemsUseCase.Input.of(paginationRequest);

        // when
        GetItemsUseCase.Output output = getItemsUseCase.execute(input);

        // then
        assertThat(output).isNotNull();
        assertThat(output.getPageResponse()).isEqualTo(pageResponse);
        assertThat(output.getPageResponse().content())
                .hasSize(1)
                .extracting(
                        content -> content.item().id(),
                        content -> content.item().name(),
                        content -> content.item().price(),
                        content -> content.item().description(),
                        content -> content.stock().id(),
                        content -> content.stock().quantity()
                )
                .containsExactly(
                        tuple(1L, "ItemA", 1000L, "this is ItemA", 1L, 100L)
                );

        then(itemReader).should(times(1)).getItems(paginationRequest);
    }
}