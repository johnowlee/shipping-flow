package com.shippingflow.presenter.api.item.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shippingflow.core.domain.aggregate.item.dto.ItemDto;
import com.shippingflow.core.domain.aggregate.item.dto.ItemWithStockDto;
import com.shippingflow.core.usecase.aggregate.item.CreateItemUseCase;
import com.shippingflow.core.usecase.aggregate.item.UpdateStockUseCase;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ItemResponse(long id, String name, Long price, Long quantity, String description) {

    //TODO : ItemWithStockDto로 통일
    public static ItemResponse from(CreateItemUseCase.Output output) {
        ItemDto itemDto = output.getItemWithStockDto().item();
        Long quantity = checkStockPresence(output);
        return new ItemResponse(itemDto.id(), itemDto.name(), itemDto.price(), quantity, itemDto.description());
    }

    public static ItemResponse from(UpdateStockUseCase.Output output) {
        ItemDto itemDto = output.getItem();
        Long quantity = output.getStock().quantity();
        return new ItemResponse(itemDto.id(), itemDto.name(), itemDto.price(), quantity, itemDto.description());
    }

    public static ItemResponse from(ItemWithStockDto dto) {
        ItemDto itemDto = dto.item();
        Long quantity = dto.stock() != null ? dto.stock().quantity() : null;
        return new ItemResponse(itemDto.id(), itemDto.name(), itemDto.price(), quantity, itemDto.description());
    }

    private static Long checkStockPresence(CreateItemUseCase.Output output) {
        return output.isStockDtoPresent() ? output.getItemWithStockDto().stock().quantity() : null;
    }
}
