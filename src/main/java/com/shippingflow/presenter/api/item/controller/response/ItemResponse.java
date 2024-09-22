package com.shippingflow.presenter.api.item.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shippingflow.core.domain.aggregate.item.dto.ItemDto;
import com.shippingflow.core.usecase.aggregate.item.CreateItemUseCase;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ItemResponse(long id, String name, Long price, Long quantity, String description) {

    public static ItemResponse from(CreateItemUseCase.Output output) {
        ItemDto itemDto = output.getItemWithStockDto().item();
        Long quantity = checkStockPresence(output);
        return new ItemResponse(itemDto.id(), itemDto.name(), itemDto.price(), quantity, itemDto.description());
    }

    private static Long checkStockPresence(CreateItemUseCase.Output output) {
        return output.isStockDtoPresent() ? output.getItemWithStockDto().stock().quantity() : null;
    }
}
