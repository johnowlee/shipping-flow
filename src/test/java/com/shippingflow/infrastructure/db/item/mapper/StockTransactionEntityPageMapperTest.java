package com.shippingflow.infrastructure.db.item.mapper;

import com.shippingflow.core.domain.aggregate.item.dto.StockTransactionDto;
import com.shippingflow.core.domain.aggregate.item.model.local.StockTransactionType;
import com.shippingflow.core.domain.common.pagination.PageResponse;
import com.shippingflow.infrastructure.db.item.entity.StockTransactionEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class StockTransactionEntityPageMapperTest {

    @DisplayName("StockTransactionEntity 타입의 Page 객체를 StockTransactionDto 타입의 PageResponse로 매핑한다.")
    @Test
    void toStockTransactionDtoPageResponse() {
        // given
        StockTransactionEntity txEntity1 = createNewStockTransactionForTest(1L, 1000L, StockTransactionType.INCREASE, LocalDateTime.now());
        StockTransactionEntity txEntity2 = createNewStockTransactionForTest(2L, 500L, StockTransactionType.DECREASE, LocalDateTime.now());
        List<StockTransactionEntity> txEntities = List.of(txEntity1, txEntity2);

        Pageable pageable = PageRequest.of(0, 5);
        Page<StockTransactionEntity> txEntityPage = new PageImpl<>(txEntities, pageable, txEntities.size());

        // when
        StockTransactionEntityPageMapper mapper = new StockTransactionEntityPageMapper();
        PageResponse<StockTransactionDto> actual = mapper.toStockTransactionDtoPageResponse(txEntityPage);

        // then
        assertThat(actual.pageNumber()).isEqualTo(0);
        assertThat(actual.pageSize()).isEqualTo(5);
        assertThat(actual.totalElements()).isEqualTo(2);
        assertThat(actual.totalPages()).isEqualTo(1);

        assertThat(actual.content())
                .hasSize(2)
                .extracting(
                        "id", "quantity", "transactionType", "transactionDateTime"
                )
                .contains(
                        tuple(txEntity1.getId(), txEntity1.getQuantity(), txEntity1.getTransactionType(), txEntity1.getTransactionDateTime()),
                        tuple(txEntity2.getId(), txEntity2.getQuantity(), txEntity2.getTransactionType(), txEntity2.getTransactionDateTime())
                );
    }

    private static StockTransactionEntity createNewStockTransactionForTest(Long id, long transactionQuantity, StockTransactionType transactionType, LocalDateTime transactionDateTime) {
        return StockTransactionEntity.builder()
                .id(id)
                .quantity(transactionQuantity)
                .transactionType(transactionType)
                .transactionDateTime(transactionDateTime)
                .build();
    }

}