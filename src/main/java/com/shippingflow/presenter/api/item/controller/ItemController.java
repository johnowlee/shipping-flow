package com.shippingflow.presenter.api.item.controller;

import com.shippingflow.core.domain.aggregate.item.model.local.StockTransactionType;
import com.shippingflow.core.usecase.aggregate.item.CreateItemUseCase;
import com.shippingflow.core.usecase.aggregate.item.DecreaseStockUseCase;
import com.shippingflow.core.usecase.aggregate.item.IncreaseStockUseCase;
import com.shippingflow.core.usecase.aggregate.item.UpdateStockUseCase;
import com.shippingflow.presenter.api.RestApiResponse;
import com.shippingflow.presenter.api.item.controller.request.CreateItemRequest;
import com.shippingflow.presenter.api.item.controller.request.UpdateItemStockRequest;
import com.shippingflow.presenter.api.item.controller.response.ItemResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.shippingflow.core.domain.aggregate.item.model.local.StockTransactionType.*;

@RequiredArgsConstructor
@RequestMapping("/api/items")
@RestController
public class ItemController {

    private final CreateItemUseCase createItemUseCase;
    private final IncreaseStockUseCase increaseStockUseCase;
    private final DecreaseStockUseCase decreaseStockUseCase;

    @PostMapping
    public RestApiResponse<ItemResponse> createItem(@Valid @RequestBody CreateItemRequest createItemRequest) {
        CreateItemUseCase.Input input = createItemRequest.toInput();
        CreateItemUseCase.Output output = createItemUseCase.execute(input);
        return RestApiResponse.created(ItemResponse.from(output));
    }

    @PostMapping("{id}/stock-update")
    public RestApiResponse<ItemResponse> updateItemStock(
            @PathVariable long id,
            @Valid @RequestBody UpdateItemStockRequest request) {
        StockTransactionType transactionType = request.convertTransactionTypeToEnum();
        UpdateStockUseCase updateStockUseCase = setUseCaseImplementBy(transactionType);
        UpdateStockUseCase.Input input = UpdateStockUseCase.Input.of(id, request.quantity());
        UpdateStockUseCase.Output output = updateStockUseCase.execute(input);
        return RestApiResponse.created(ItemResponse.from(output));
    }

    private UpdateStockUseCase setUseCaseImplementBy(StockTransactionType transactionType) {
        return transactionType == INCREASE ? increaseStockUseCase : decreaseStockUseCase;
    }
}
