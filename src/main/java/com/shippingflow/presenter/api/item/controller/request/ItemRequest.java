package com.shippingflow.presenter.api.item.controller.request;

import com.shippingflow.core.usecase.aggregate.item.CreateItemUseCase;
import jakarta.validation.constraints.NotBlank;

public record ItemRequest(

        @NotBlank
        String name,
        Long price,
        String description,
        Long quantity
) {

    public CreateItemUseCase.Input toInput() {
        return CreateItemUseCase.Input.of(name, price, description, quantity);
    }
}
