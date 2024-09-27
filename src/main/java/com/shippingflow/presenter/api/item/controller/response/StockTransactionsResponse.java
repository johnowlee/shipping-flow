package com.shippingflow.presenter.api.item.controller.response;

import com.shippingflow.core.domain.aggregate.item.dto.StockTransactionDto;
import com.shippingflow.core.domain.common.pagination.PageResponse;
import com.shippingflow.core.usecase.aggregate.item.GetStockTransactionsUseCase;
import com.shippingflow.presenter.api.common.response.PageApiResponse;

import java.util.List;

public record StockTransactionsResponse(
        List<StockTransactionResponse> stockTransactions,
        PageApiResponse page) {

    public static StockTransactionsResponse from(GetStockTransactionsUseCase.Output output) {
        PageResponse<StockTransactionDto> pageResponse = output.getPageResponse();
        return new StockTransactionsResponse(
                toStockTransactionResponseList(pageResponse.content()),
                toPageApiResponse(pageResponse)
        );
    }

    private static PageApiResponse toPageApiResponse(PageResponse<StockTransactionDto> pageResponse) {
        return PageApiResponse.of(pageResponse.pageNumber(), pageResponse.pageSize(), pageResponse.totalElements(), pageResponse.totalPages());
    }

    private static List<StockTransactionResponse> toStockTransactionResponseList(List<StockTransactionDto> content) {
        return content.stream()
                .map(StockTransactionResponse::from)
                .toList();
    }
}
