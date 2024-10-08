package com.shippingflow.core.usecase.aggregate.item;

import com.shippingflow.core.domain.aggregate.item.component.ItemReader;
import com.shippingflow.core.domain.aggregate.item.component.ItemWriter;
import com.shippingflow.core.domain.aggregate.item.dto.ItemWithStockDto;
import com.shippingflow.core.domain.aggregate.item.model.local.StockTransactionType;
import com.shippingflow.core.domain.aggregate.item.model.root.Item;
import com.shippingflow.core.usecase.UseCase;
import com.shippingflow.core.usecase.common.ClockManager;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
public abstract class UpdateStockUseCase extends UseCase<UpdateStockUseCase.Input, UpdateStockUseCase.Output> {

    private final ItemReader itemReader;
    private final ItemWriter itemWriter;
    private final ClockManager clockManager;
    private final StockTransactionType stockTransactionType;

    @Override
    public Output execute(Input input) {
        Item foundItem = findItem(input.getItemId());
        Item updatedItem = updateStock(foundItem, input.getQuantity());
        return toOutput(updatedItem);
    }

    public StockTransactionType getTransactionType() {
        return stockTransactionType;
    }

    private Item findItem(long itemId) {
        return itemReader.getItemWithStockById(itemId);
    }

    private Item updateStock(Item item, long quantity) {
        updateStockQuantity(item, quantity);
        recordStockTransaction(item, quantity);
        return persist(item);
    }

    protected abstract void updateStockQuantity(Item item, long quantity);

    private void recordStockTransaction(Item item, long quantity) {
        item.recordStockTransaction(stockTransactionType, quantity, clockManager.getNowDateTime());
    }

    private Item persist(Item item) {
        return itemWriter.updateStock(item);
    }

    private static Output toOutput(Item item) {
        return Output.of(item.toItemWithStockDto());
    }

    @Value(staticConstructor = "of")
    public static class Input implements UseCase.Input {
        long itemId;
        long quantity;
    }

    @Value(staticConstructor = "of")
    public static class Output implements UseCase.Output {
        ItemWithStockDto itemWithStockDto;
    }
}
