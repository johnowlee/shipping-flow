package com.shippingflow.presenter.api.item.controller;

import com.shippingflow.core.domain.aggregate.item.dto.ItemDto;
import com.shippingflow.core.domain.aggregate.item.dto.ItemWithStockDto;
import com.shippingflow.core.domain.aggregate.item.dto.StockDto;
import com.shippingflow.core.domain.aggregate.item.model.local.StockTransactionType;
import com.shippingflow.core.usecase.aggregate.item.*;
import com.shippingflow.presenter.WebMvcTestSupport;
import com.shippingflow.presenter.api.item.controller.request.CreateItemRequest;
import com.shippingflow.presenter.api.item.controller.request.UpdateItemStockRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
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
    UpdateStockUseCaseFactory updateStockUseCaseFactory;

    @MockBean
    IncreaseStockUseCase increaseStockUseCase;

    @Mock
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

    @DisplayName("상품을 입고한다.")
    @Test
    void updateItemStock_increase() throws Exception {
        // given
        long itemId = 1L;
        String stockTransactionType = "INCREASE";
        long quantity = 1000L;
        UpdateItemStockRequest updateItemStockRequest = new UpdateItemStockRequest(stockTransactionType, quantity);

        given(updateStockUseCaseFactory.getUseCaseBy(any(StockTransactionType.class))).willReturn(increaseStockUseCase);
        UpdateStockUseCase updateStockUseCase = increaseStockUseCase;

        String itemName = "ItemA";
        long price = 1000L;
        String description = "This is ItemA";
        ItemDto itemDto = new ItemDto(itemId, itemName, price, description);
        StockDto stockDto = new StockDto(1L, quantity);

        UpdateStockUseCase.Output output = mock(UpdateStockUseCase.Output.class);
        given(output.getItem()).willReturn(itemDto);
        given(output.getStock()).willReturn(stockDto);
        given(updateStockUseCase.execute(any(UpdateStockUseCase.Input.class))).willReturn(output);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/items/{id}/stock-update", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateItemStockRequest))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value(itemName))
                .andExpect(jsonPath("$.data.price").value(price))
                .andExpect(jsonPath("$.data.quantity").value(quantity))
                .andExpect(jsonPath("$.data.description").value(description));
    }

    @DisplayName("상품을 출고한다.")
    @Test
    void updateItemStock_decrease() throws Exception {
        // given
        long itemId = 1L;
        String stockTransactionType = "DECREASE";
        long quantity = 1000L;
        UpdateItemStockRequest updateItemStockRequest = new UpdateItemStockRequest(stockTransactionType, quantity);

        given(updateStockUseCaseFactory.getUseCaseBy(any(StockTransactionType.class))).willReturn(decreaseStockUseCase);
        UpdateStockUseCase updateStockUseCase = decreaseStockUseCase;

        String itemName = "ItemA";
        long price = 1000L;
        String description = "This is ItemA";
        ItemDto itemDto = new ItemDto(itemId, itemName, price, description);
        StockDto stockDto = new StockDto(1L, quantity);

        UpdateStockUseCase.Output output = mock(UpdateStockUseCase.Output.class);
        given(output.getItem()).willReturn(itemDto);
        given(output.getStock()).willReturn(stockDto);
        given(updateStockUseCase.execute(any(UpdateStockUseCase.Input.class))).willReturn(output);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/items/{id}/stock-update", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateItemStockRequest))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value(itemName))
                .andExpect(jsonPath("$.data.price").value(price))
                .andExpect(jsonPath("$.data.quantity").value(quantity))
                .andExpect(jsonPath("$.data.description").value(description));
    }

    @DisplayName("상품을 입출고할 때 입출고 타입이 유효하지 않으면 예외가 발생한다.")
    @ParameterizedTest
    @MethodSource("getInvalidStockTransactionTypes")
    void updateItemStock_shouldThrowExceptionWhenStockTransactionTypeIsInvalid(String invalidTypes) throws Exception {
        // given
        long itemId = 1L;
        long quantity = 1000L;
        UpdateItemStockRequest updateItemStockRequest = new UpdateItemStockRequest(invalidTypes, quantity);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/items/{id}/stock-update", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateItemStockRequest))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.name").value("stockTransactionType"))
                .andExpect(jsonPath("$.message").value("입출고 타입이 유효하지 않습니다."));
    }

    @DisplayName("상품을 입출고할 때 입출고 수량이 1개 보다 적으면 예외가 발생한다.")
    @Test
    void updateItemStock_shouldThrowExceptionWhenQuantityIsNegative() throws Exception {
        // given
        long itemId = 1L;
        long quantity = 0;
        UpdateItemStockRequest updateItemStockRequest = new UpdateItemStockRequest("INCREASE", quantity);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/items/{id}/stock-update", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateItemStockRequest))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.name").value("quantity"))
                .andExpect(jsonPath("$.message").value("must be greater than 0"));
    }

    private static List<String> getBlanks() {
        return Arrays.asList("", " ", null);
    }

    private static List<String> getInvalidStockTransactionTypes() {
        return Arrays.asList("", " ", null, "INVALID_TYPE");
    }
}