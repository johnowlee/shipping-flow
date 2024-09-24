package com.shippingflow.presenter.api.item.controller.response;

import com.shippingflow.core.domain.aggregate.item.dto.ItemWithStockDto;
import com.shippingflow.core.domain.common.pagination.PageResponse;

import java.util.List;

public record ItemsResponse(List<ItemResponse> items, com.shippingflow.presenter.api.item.common.response.PageResponse page) {

    public static ItemsResponse from(PageResponse<ItemWithStockDto> pageResponse) {
        return of(toItemResponseListFrom(pageResponse), com.shippingflow.presenter.api.item.common.response.PageResponse.from(pageResponse));
    }

    private static List<ItemResponse> toItemResponseListFrom(PageResponse<ItemWithStockDto> pageResponse) {
        List<ItemWithStockDto> content = pageResponse.content();
        return content.stream()
                .map(ItemResponse::from)
                .toList();
    }

    private static ItemsResponse of(List<ItemResponse> items, com.shippingflow.presenter.api.item.common.response.PageResponse page) {
        return new ItemsResponse(items, page);
    }
}
