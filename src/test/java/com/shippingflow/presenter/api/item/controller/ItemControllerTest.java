package com.shippingflow.presenter.api.item.controller;

import com.shippingflow.core.domain.aggregate.item.dto.ItemDto;
import com.shippingflow.core.domain.aggregate.item.dto.ItemWithStockDto;
import com.shippingflow.core.domain.aggregate.item.dto.StockDto;
import com.shippingflow.core.usecase.aggregate.item.CreateItemUseCase;
import com.shippingflow.core.usecase.aggregate.item.DecreaseStockUseCase;
import com.shippingflow.core.usecase.aggregate.item.IncreaseStockUseCase;
import com.shippingflow.presenter.WebMvcTestSupport;
import com.shippingflow.presenter.api.item.controller.request.CreateItemRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ItemControllerTest extends WebMvcTestSupport {

    @MockBean
    CreateItemUseCase createItemUseCase;

    @MockBean
    IncreaseStockUseCase increaseStockUseCase;

    @MockBean
    DecreaseStockUseCase decreaseStockUseCase;

    @DisplayName("상품을 등록한다.")
    @Test
    void createItem() throws Exception {
        // given
        String itemName = "ItemA";
        long price = 1000L;
        long quantity = 5000L;
        String description = "This is ItemA";
        CreateItemRequest createItemRequest = new CreateItemRequest(itemName, price, quantity, description);

        ItemDto itemDto = new ItemDto(1L, itemName, price, description);
        StockDto stockDto = new StockDto(1L, quantity);
        ItemWithStockDto itemWithStockDto = new ItemWithStockDto(itemDto, stockDto);

        CreateItemUseCase.Output output = mock(CreateItemUseCase.Output.class);

        given(output.getItemWithStockDto()).willReturn(itemWithStockDto);
        given(createItemUseCase.execute(any(CreateItemUseCase.Input.class))).willReturn(output);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createItemRequest))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("201"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value(itemName))
                .andExpect(jsonPath("$.data.price").value(price))
                .andExpect(jsonPath("$.data.description").value(description));
    }

    @DisplayName("상품을 등록할때 상품명이 빈값이거나 null이면 예외가 발생한다.")
    @ParameterizedTest
    @MethodSource("getBlanks")
    void createItem_shouldThrowExceptionWhenItemNameIsEmptyOrNull(String itemName) throws Exception {
        // given
        long price = 1000L;
        String description = "This is ItemA";
        CreateItemRequest createItemRequest = new CreateItemRequest(itemName, price, 1000L, description);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createItemRequest))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.message").value("must not be blank"));
    }

    private static List<String> getBlanks() {
        return Arrays.asList("", " ", null);
    }
}