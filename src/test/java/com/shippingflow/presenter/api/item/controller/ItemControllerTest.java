//package com.shippingflow.presenter.api.item.controller;
//
//import com.shippingflow.core.aggregate.item.root.Item;
//import com.shippingflow.core.usecase.aggregate.item.CreateItemUseCase;
//import com.shippingflow.presenter.WebMvcTestSupport;
//import com.shippingflow.presenter.api.item.controller.request.ItemRequest;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.mock;
//import static org.springframework.http.HttpStatus.BAD_REQUEST;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//class ItemControllerTest extends WebMvcTestSupport {
//
//    @MockBean
//    CreateItemUseCase createItemUseCase;
//
//    @DisplayName("상품을 등록한다.")
//    @Test
//    void createItem() throws Exception {
//        // given
//        String itemName = "ItemA";
//        long price = 1000L;
//        String description = "This is ItemA";
//        ItemRequest itemRequest = new ItemRequest(itemName, price, description);
//
//        Item item = Item.builder()
//                .id(1L)
//                .name(itemRequest.name())
//                .price(itemRequest.price())
//                .description(itemRequest.description())
//                .build();
//
//        CreateItemUseCase.Output output = mock(CreateItemUseCase.Output.class);
//
//        given(output.getItem()).willReturn(item);
//        given(createItemUseCase.execute(any(CreateItemUseCase.Input.class))).willReturn(output);
//
//        // when & then
//        mockMvc.perform(MockMvcRequestBuilders.post("/items")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(itemRequest))
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value("201"))
//                .andExpect(jsonPath("$.data.id").value(1L))
//                .andExpect(jsonPath("$.data.name").value(itemName))
//                .andExpect(jsonPath("$.data.price").value(price))
//                .andExpect(jsonPath("$.data.description").value(description));
//    }
//
//    @DisplayName("상품을 등록할때 상품명이 빈값이거나 null이면 예외가 발생한다.")
//    @ParameterizedTest
//    @MethodSource("getBlanks")
//    void createItem_shouldThrowExceptionWhenItemNameIsEmptyOrNull(String itemName) throws Exception {
//        // given
//        long price = 1000L;
//        String description = "This is ItemA";
//        ItemRequest itemRequest = new ItemRequest(itemName, price, description);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/items")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(itemRequest))
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value("400"))
//                .andExpect(jsonPath("$.status").value(BAD_REQUEST.name()))
//                .andExpect(jsonPath("$.name").value("name"))
//                .andExpect(jsonPath("$.message").value("must not be blank"));
//    }
//
//    private static List<String> getBlanks() {
//        return Arrays.asList("", " ", null);
//    }
//}