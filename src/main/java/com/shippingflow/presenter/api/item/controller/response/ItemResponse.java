package com.shippingflow.presenter.api.item.controller.response;

import com.shippingflow.core.aggregate.domain.item.dto.ItemDto;
import com.shippingflow.core.usecase.aggregate.item.CreateItemUseCase;

public record ItemResponse(long id, String name, Long price, String description) {

    public static ItemResponse from(CreateItemUseCase.Output output) {
        ItemDto itemDto = output.getItemWithStockDto().item();
        return new ItemResponse(itemDto.id(), itemDto.name(), itemDto.price(), itemDto.description());
    }
}
