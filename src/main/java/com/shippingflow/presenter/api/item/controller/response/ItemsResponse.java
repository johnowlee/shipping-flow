package com.shippingflow.presenter.api.item.controller.response;

import com.shippingflow.core.domain.aggregate.item.dto.ItemWithStockDto;
import com.shippingflow.core.domain.common.pagination.PageResponse;
import com.shippingflow.presenter.api.common.response.PageApiResponse;

import java.util.List;

public record ItemsResponse(List<ItemResponse> items, PageApiResponse page) {

    public static ItemsResponse from(PageResponse<ItemWithStockDto> pageResponse) {
        return new ItemsResponse(
                toItemResponseListFrom(pageResponse.content()),
                toPageApiResponse(pageResponse)
        );
    }

    private static List<ItemResponse> toItemResponseListFrom(List<ItemWithStockDto> itemWithStockDtoList) {
        return itemWithStockDtoList.stream()
                .map(ItemResponse::from)
                .toList();
    }

    private static PageApiResponse toPageApiResponse(PageResponse<ItemWithStockDto> pageResponse) {
        return PageApiResponse.of(pageResponse.pageNumber(), pageResponse.pageSize(), pageResponse.totalElements(), pageResponse.totalPages());
    }
}
