package com.shippingflow.presenter.api.item.controller;

import com.shippingflow.core.domain.common.pagination.SortablePaginationRequest;
import com.shippingflow.core.usecase.aggregate.item.*;
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
    private final GetItemUseCase getItemUseCase;
    private final GetItemsUseCase getItemsUseCase;

    @PostMapping
    public RestApiResponse<ItemResponse> createItem(@Valid @RequestBody CreateItemRequest createItemRequest) {
        CreateItemUseCase.Input input = createItemRequest.toInput();
        CreateItemUseCase.Output output = createItemUseCase.execute(input);
        return RestApiResponse.created(ItemResponse.from(output.getItemWithStockDto()));
    }
    
    @PostMapping("{id}/stock-update")
    public RestApiResponse<ItemResponse> updateItemStock(@PathVariable long id, @Valid @RequestBody UpdateItemStockRequest request) {
        UpdateStockUseCase updateStockUseCase = updateStockUseCaseFactory.getUseCaseBy(request.convertStockTransactionTypeToEnum());
        UpdateStockUseCase.Input input = UpdateStockUseCase.Input.of(id, request.quantity());
        UpdateStockUseCase.Output output = updateStockUseCase.execute(input);
        return RestApiResponse.ok(ItemResponse.from(output.getItemWithStockDto()));
    }

    @GetMapping("{id}")
    public RestApiResponse<ItemResponse> getItem(@PathVariable long id) {
        GetItemUseCase.Input input = GetItemUseCase.Input.of(id);
        GetItemUseCase.Output output = getItemUseCase.execute(input);
        return RestApiResponse.ok(ItemResponse.from(output.getItemWithStockDto()));
    }

    @GetMapping
    public RestApiResponse<ItemsResponse> getItems(
            @RequestParam @Min(1) int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        GetItemsUseCase.Input input = GetItemsUseCase.Input.of(SortablePaginationRequest.of(pageNumber, pageSize, sortBy, sortDir));
        GetItemsUseCase.Output output = getItemsUseCase.execute(input);
        return RestApiResponse.ok(ItemsResponse.from(output.getPageResponse()));
    }

    // TODO: 2024-09-26 재고 내역 조회 구현
}
