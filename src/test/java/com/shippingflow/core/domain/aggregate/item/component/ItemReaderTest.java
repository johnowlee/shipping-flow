package com.shippingflow.core.domain.aggregate.item.component;

import com.shippingflow.core.domain.aggregate.item.dto.ItemDto;
import com.shippingflow.core.domain.aggregate.item.dto.ItemWithStockDto;
import com.shippingflow.core.domain.aggregate.item.dto.StockDto;
import com.shippingflow.core.domain.aggregate.item.dto.StockTransactionDto;
import com.shippingflow.core.domain.aggregate.item.model.local.StockTransactionType;
import com.shippingflow.core.domain.aggregate.item.model.root.Item;
import com.shippingflow.core.domain.aggregate.item.repository.ItemReaderRepository;
import com.shippingflow.core.domain.common.pagination.BasicPaginationRequest;
import com.shippingflow.core.domain.common.pagination.PageResponse;
import com.shippingflow.core.domain.common.pagination.SortablePaginationRequest;
import com.shippingflow.core.exception.DomainException;
import com.shippingflow.core.exception.error.ItemError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
        assertThatThrownBy(() -> itemReader.getItemWithStockById(1L))
                .isInstanceOf(DomainException.class)
                .hasMessage(ItemError.NOT_FOUND_ITEM.getMessage());
        then(itemReaderRepository).should(times(1)).findItemWithStockById(eq(1L));
    }

    @DisplayName("페이징된 상품 목록을 조회한다.")
    @Test
    void getItems() {
        // given
        ItemDto itemDto = ItemDto.of(1L, "itemA", 1000L, "this is ItemA");
        StockDto stockDto = StockDto.of(1L, 5000L);
        ItemWithStockDto itemWithStockDto = ItemWithStockDto.of(itemDto, stockDto);

        int pageNumber = 1;
        int pageSize = 5;
        int totalElements = 1;
        int totalPages = 1;
        PageResponse<ItemWithStockDto> pageResponse = new PageResponse<>(List.of(itemWithStockDto), pageNumber, pageSize, totalElements, totalPages);
        SortablePaginationRequest sortablePaginationRequest = SortablePaginationRequest.of(1, 1, null, null);

        given(itemReaderRepository.findAllItemsWithStock(sortablePaginationRequest)).willReturn(pageResponse);


        // when
        PageResponse<ItemWithStockDto> actual = itemReader.getItems(sortablePaginationRequest);

        // then
        assertThat(actual.content()).hasSize(1)
                .extracting(ItemWithStockDto::item, ItemWithStockDto::stock)
                .containsExactly(tuple(itemDto, stockDto));
        assertThat(actual.pageNumber()).isEqualTo(pageNumber);
        assertThat(actual.pageSize()).isEqualTo(pageSize);
        assertThat(actual.totalElements()).isEqualTo(totalElements);
        assertThat(actual.totalPages()).isEqualTo(totalPages);
    }

    @DisplayName("페이징된 상품 재고 내역을 조회한다.")
    @Test
    void getStockTransactions() {
        // given
        StockTransactionDto stockTransactionDto1 = StockTransactionDto.of(1L, 5000L, StockTransactionType.INCREASE, LocalDateTime.now());
        StockTransactionDto stockTransactionDto2 = StockTransactionDto.of(2L, 3000L, StockTransactionType.DECREASE, LocalDateTime.now());

        int pageNumber = 1;
        int pageSize = 5;
        int totalElements = 1;
        int totalPages = 1;
        PageResponse<StockTransactionDto> pageResponse = new PageResponse<>(List.of(stockTransactionDto1, stockTransactionDto2), pageNumber, pageSize, totalElements, totalPages);
        BasicPaginationRequest basicPaginationRequest = BasicPaginationRequest.of(1, 1);

        given(itemReaderRepository.findStockTransactionsByItemId(1L, basicPaginationRequest)).willReturn(pageResponse);

        // when
        PageResponse<StockTransactionDto> actual = itemReader.getStockTransactions(1L, basicPaginationRequest);

        // then
        assertThat(actual.content()).hasSize(2)
                .containsExactly(stockTransactionDto1, stockTransactionDto2);
        assertThat(actual.pageNumber()).isEqualTo(pageNumber);
        assertThat(actual.pageSize()).isEqualTo(pageSize);
        assertThat(actual.totalElements()).isEqualTo(totalElements);
        assertThat(actual.totalPages()).isEqualTo(totalPages);

        then(itemReaderRepository).should(times(1)).findStockTransactionsByItemId(anyLong(), any(BasicPaginationRequest.class));
    }
}