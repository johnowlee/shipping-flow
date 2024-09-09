package com.shippingflow.presenter.api.item.controller.response;

import com.shippingflow.core.domain.item.Item;
import com.shippingflow.core.usecase.item.CreateItemUseCase;

public record ItemResponse(long id, String name, Long price, String description) {

    public static ItemResponse from(CreateItemUseCase.Output output) {
        Item item = output.getItem();
        return new ItemResponse(item.getId(), item.getName(), item.getPrice(), item.getDescription());
    }
}
