package com.shippingflow.core.usecase.aggregate.item;

import com.shippingflow.core.domain.aggregate.item.repository.ItemReaderRepository;
import com.shippingflow.core.domain.aggregate.item.repository.ItemWriterRepository;
import com.shippingflow.core.domain.aggregate.item.root.Item;
import com.shippingflow.core.exception.DomainException;
import com.shippingflow.core.exception.error.ItemError;
import com.shippingflow.core.usecase.UseCase;
import com.shippingflow.core.usecase.aggregate.item.vo.ItemVo;
import com.shippingflow.core.usecase.common.ClockManager;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
public abstract class UpdateStockUseCase extends UseCase<UpdateStockUseCase.Input, UpdateStockUseCase.Output> {

    private final ItemReaderRepository itemReaderRepository;
    private final ItemWriterRepository itemWriterRepository;
    private final ClockManager clockManager;

    @Override
    public Output execute(Input input) {
        Item foundItem = findItem(input.getItemId());
        updateStock(foundItem, input.getQuantity());
        return toOutput(foundItem);
    }

    private Item findItem(long itemId) {
        return itemReaderRepository.findById(itemId)
                .orElseThrow(() -> DomainException.from(ItemError.NOT_FOUND_ITEM));
    }

    private void updateStock(Item foundItem, long quantity) {
        updateStockQuantity(foundItem, quantity);
        recordStockTransaction(foundItem, quantity, clockManager);
        persist(foundItem);
    }

    private Item persist(Item item) {
        return itemWriterRepository.update(item.toVo());
    }

    private static Output toOutput(Item item) {
        return Output.of(item.toVo());
    }

    protected abstract Item updateStockQuantity(Item item, long quantity);

    protected abstract Item recordStockTransaction(Item item, long quantity, ClockManager clockManager);
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
