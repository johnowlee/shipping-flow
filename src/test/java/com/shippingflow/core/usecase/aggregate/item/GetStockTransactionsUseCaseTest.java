package com.shippingflow.core.usecase.aggregate.item;

import com.shippingflow.core.domain.aggregate.item.component.ItemReader;
import com.shippingflow.core.domain.aggregate.item.dto.StockTransactionDto;
import com.shippingflow.core.domain.aggregate.item.model.local.StockTransactionType;
import com.shippingflow.core.domain.common.pagination.BasicPaginationRequest;
import com.shippingflow.core.domain.common.pagination.PageResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class GetStockTransactionsUseCaseTest {

    @Mock
    ItemReader itemReader;

    @InjectMocks
    GetStockTransactionsUseCase getStockTransactionsUseCase;

    @DisplayName("페이징된 재고 내역을 조회한다.")
    @Test
    void execute() {
        // given
        StockTransactionDto stockTransactionDto1 = StockTransactionDto.of(1L, 5000L, StockTransactionType.INCREASE, LocalDateTime.now());
        StockTransactionDto stockTransactionDto2 = StockTransactionDto.of(2L, 3000L, StockTransactionType.DECREASE, LocalDateTime.now());

        int pageNumber = 1;
        int pageSize = 5;
        int totalElements = 1;
        int totalPages = 1;
        PageResponse<StockTransactionDto> pageResponse = new PageResponse<>(List.of(stockTransactionDto1, stockTransactionDto2), pageNumber, pageSize, totalElements, totalPages);
        BasicPaginationRequest basicPaginationRequest = BasicPaginationRequest.of(1, 1);

        given(itemReader.getStockTransactions(1L, basicPaginationRequest)).willReturn(pageResponse);

        // when
        PageResponse<StockTransactionDto> actual = itemReader.getStockTransactions(1L, basicPaginationRequest);

        // then
        assertThat(actual.content()).hasSize(2)
                .containsExactly(stockTransactionDto1, stockTransactionDto2);
        assertThat(actual.pageNumber()).isEqualTo(pageNumber);
        assertThat(actual.pageSize()).isEqualTo(pageSize);
        assertThat(actual.totalElements()).isEqualTo(totalElements);
        assertThat(actual.totalPages()).isEqualTo(totalPages);

        then(itemReader).should(times(1)).getStockTransactions(anyLong(), any(BasicPaginationRequest.class));
    }

}