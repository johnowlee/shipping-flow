package com.shippingflow.presenter.api.item.controller.request;

import com.shippingflow.core.usecase.item.CreateItemUseCase;
import jakarta.validation.constraints.NotBlank;

public record ItemRequest(

        @NotBlank
        String name,
        Long price,
        String description
) {

    public CreateItemUseCase.Input toInput() {
        return new CreateItemUseCase.Input(name, price, description);
    }
}
