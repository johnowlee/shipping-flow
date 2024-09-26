package com.shippingflow.infrastructure.db.item.mapper;

import com.shippingflow.core.domain.aggregate.item.dto.StockTransactionDto;
import com.shippingflow.core.domain.common.pagination.PageResponse;
import com.shippingflow.infrastructure.db.item.entity.StockTransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StockTransactionEntityPageMapper {

    public PageResponse<StockTransactionDto> toStockTransactionDtoPageResponse(Page<StockTransactionEntity> stockTransactionEntityPage) {
        return new PageResponse<>(
                toStockTransactionDtoList(stockTransactionEntityPage.getContent()),
                stockTransactionEntityPage.getNumber(),
                stockTransactionEntityPage.getSize(),
                stockTransactionEntityPage.getTotalElements(),
                stockTransactionEntityPage.getTotalPages()
        );
    }

    private static List<StockTransactionDto> toStockTransactionDtoList(List<StockTransactionEntity> stockTransactionEntities) {
        return stockTransactionEntities.stream()
                .map(StockTransactionEntity::toStockTransactionDto)
                .toList();
    }
}
