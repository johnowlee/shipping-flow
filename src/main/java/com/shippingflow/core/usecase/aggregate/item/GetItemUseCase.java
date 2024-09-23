package com.shippingflow.core.usecase.aggregate.item;

import com.shippingflow.core.domain.aggregate.item.component.ItemReader;
import com.shippingflow.core.domain.aggregate.item.dto.ItemWithStockDto;
import com.shippingflow.core.domain.aggregate.item.model.root.Item;
import com.shippingflow.core.usecase.UseCase;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class GetItemUseCase extends UseCase<GetItemUseCase.Input, GetItemUseCase.Output> {

    private final ItemReader itemReader;

    @Override
    public Output execute(Input input) {
        Item item = itemReader.getItemWithStockById(input.getId());
        return Output.of(item.toItemWithStockDto());
    }

    @Value(staticConstructor = "of")
    public static class Input implements UseCase.Input {
        long id;
    }

    @Value(staticConstructor = "of")
    public static class Output implements UseCase.Output {
        ItemWithStockDto itemWithStockDto;
    }
}
