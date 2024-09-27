package com.shippingflow.presenter.api.item.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shippingflow.core.domain.aggregate.item.dto.ItemDto;
import com.shippingflow.core.domain.aggregate.item.dto.ItemWithStockDto;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ItemResponse(long id, String name, Long price, Long quantity, String description) {

    public static ItemResponse from(ItemWithStockDto dto) {
        ItemDto itemDto = dto.item();
        Long quantity = dto.stock() != null ? dto.stock().quantity() : null;
        return new ItemResponse(itemDto.id(), itemDto.name(), itemDto.price(), quantity, itemDto.description());
    }
}
