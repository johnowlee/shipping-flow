package com.shippingflow.presenter.api.item.controller;

import com.shippingflow.core.usecase.item.CreateItemUseCase;
import com.shippingflow.presenter.api.RestApiResponse;
import com.shippingflow.presenter.api.item.controller.request.ItemRequest;
import com.shippingflow.presenter.api.item.controller.response.ItemResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/items")
@RequiredArgsConstructor
@RestController
public class ItemController {

    private final CreateItemUseCase createItemUseCase;

    @PostMapping
    public RestApiResponse<ItemResponse> createItem(@Valid @RequestBody ItemRequest itemRequest) {
        CreateItemUseCase.Input input = itemRequest.toInput();
        CreateItemUseCase.Output output = createItemUseCase.execute(input);
        return RestApiResponse.created(ItemResponse.from(output));
    }
}
