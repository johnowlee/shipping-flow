package com.shippingflow.presenter.api.item.controller;

import com.shippingflow.core.domain.common.pagination.SortablePaginationRequest;
import com.shippingflow.core.usecase.aggregate.item.CreateItemUseCase;
import com.shippingflow.core.usecase.aggregate.item.GetItemsUseCase;
import com.shippingflow.core.usecase.aggregate.item.UpdateStockUseCase;
import com.shippingflow.core.usecase.aggregate.item.UpdateStockUseCaseFactory;
import com.shippingflow.presenter.api.RestApiResponse;
import com.shippingflow.presenter.api.item.controller.request.CreateItemRequest;
import com.shippingflow.presenter.api.item.controller.request.UpdateItemStockRequest;
import com.shippingflow.presenter.api.item.controller.response.ItemResponse;
import com.shippingflow.presenter.api.item.controller.response.ItemsResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/items")
@RestController
public class ItemController {

    private final CreateItemUseCase createItemUseCase;
    private final UpdateStockUseCaseFactory updateStockUseCaseFactory;
    private final GetItemsUseCase getItemsUseCase;

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

    // TODO: getItem API 작성

    // TODO: Test
    @GetMapping
    public RestApiResponse<ItemsResponse> getItems(
            @RequestParam @Min(1) int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        GetItemsUseCase.Input input = GetItemsUseCase.Input.of(SortablePaginationRequest.of(page, size, sortBy, sortDir));
        GetItemsUseCase.Output output = getItemsUseCase.execute(input);
        return RestApiResponse.ok(ItemsResponse.from(output.getPageResponse()));
    }
}
