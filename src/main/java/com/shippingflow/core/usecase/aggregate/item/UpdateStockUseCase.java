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
        long quantity = input.getQuantity();
        return itemReaderRepository.findById(input.getItemId())
                .map(item -> updateStockQuantity(item, quantity))
                .map(item -> recordStockTransaction(item, quantity, clockManager))
                .map(this::persist)
                .map(UpdateStockUseCase::toOutput)
                .orElseThrow(() -> DomainException.from(ItemError.NOT_FOUND_ITEM));
    }

    private static Output toOutput(Item item) {
        return Output.of(item.toVo());
    }

    private Item persist(Item item) {
        return itemWriterRepository.update(item.toVo());
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
