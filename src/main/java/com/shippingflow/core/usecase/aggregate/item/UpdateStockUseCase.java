package com.shippingflow.core.usecase.aggregate.item;

import com.shippingflow.core.aggregate.domain.item.component.ItemReader;
import com.shippingflow.core.aggregate.domain.item.component.ItemWriter;
import com.shippingflow.core.aggregate.domain.item.root.Item;
import com.shippingflow.core.aggregate.vo.ItemVo;
import com.shippingflow.core.usecase.UseCase;
import com.shippingflow.core.usecase.common.ClockManager;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
public abstract class UpdateStockUseCase extends UseCase<UpdateStockUseCase.Input, UpdateStockUseCase.Output> {

    private final ItemReader itemReader;
    private final ItemWriter itemWriter;
    private final ClockManager clockManager;

    @Override
    public Output execute(Input input) {
        Item foundItem = findItem(input.getItemId());
        Item updatedItem = updateStock(foundItem, input.getQuantity());
        return toOutput(updatedItem);
    }

    private Item findItem(long itemId) {
        return itemReader.findItemById(itemId);
    }

    private Item updateStock(Item foundItem, long quantity) {
        updateStockQuantity(foundItem, quantity);
        recordStockTransaction(foundItem, quantity, clockManager);
        return persist(foundItem);
    }

    private Item persist(Item item) {
        return itemWriter.update(item);
    }

    private static Output toOutput(Item item) {
        return Output.of(item.toVo());
    }

    protected abstract void updateStockQuantity(Item item, long quantity);

    protected abstract void recordStockTransaction(Item item, long quantity, ClockManager clockManager);
    @Value(staticConstructor = "of")
    public static class Input implements UseCase.Input {

        long itemId;
        long quantity;
    }

    @Value(staticConstructor = "of")
    public static class Output implements UseCase.Output {
        ItemVo item;
    }
}
