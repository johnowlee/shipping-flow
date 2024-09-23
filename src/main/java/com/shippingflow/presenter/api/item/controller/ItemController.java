package com.shippingflow.presenter.api.item.controller;

import com.shippingflow.core.usecase.aggregate.item.CreateItemUseCase;
import com.shippingflow.core.usecase.aggregate.item.UpdateStockUseCase;
import com.shippingflow.core.usecase.aggregate.item.UpdateStockUseCaseFactory;
import com.shippingflow.presenter.api.RestApiResponse;
import com.shippingflow.presenter.api.item.controller.request.CreateItemRequest;
import com.shippingflow.presenter.api.item.controller.request.UpdateItemStockRequest;
import com.shippingflow.presenter.api.item.controller.response.ItemResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/items")
@RestController
public class ItemController {

    private final CreateItemUseCase createItemUseCase;
    private final UpdateStockUseCaseFactory updateStockUseCaseFactory;

    @PostMapping
    public RestApiResponse<ItemResponse> createItem(@Valid @RequestBody CreateItemRequest createItemRequest) {
        CreateItemUseCase.Input input = createItemRequest.toInput();
        CreateItemUseCase.Output output = createItemUseCase.execute(input);
        return RestApiResponse.created(ItemResponse.from(output));
    }
    
    @PostMapping("{id}/stock-update")
    public RestApiResponse<ItemResponse> updateItemStock(@PathVariable long id, @Valid @RequestBody UpdateItemStockRequest request) {
        UpdateStockUseCase updateStockUseCase = updateStockUseCaseFactory.getUseCaseBy(request.convertStockTransactionTypeToEnum());
        UpdateStockUseCase.Input input = UpdateStockUseCase.Input.of(id, request.quantity());
        UpdateStockUseCase.Output output = updateStockUseCase.execute(input);
        return RestApiResponse.ok(ItemResponse.from(output));
    }
}
