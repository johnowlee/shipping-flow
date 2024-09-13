package com.shippingflow.core.usecase.aggregate.item;

import com.shippingflow.core.domain.aggregate.item.component.ItemValidator;
import com.shippingflow.core.domain.aggregate.item.repository.ItemWriterRepository;
import com.shippingflow.core.domain.aggregate.item.root.Item;
import com.shippingflow.core.usecase.UseCase;
import com.shippingflow.core.usecase.aggregate.item.vo.ItemVo;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CreateItemUseCase extends UseCase<CreateItemUseCase.Input, CreateItemUseCase.Output> {

    private final ItemValidator itemValidator;
    private final ItemWriterRepository itemWriterRepository;

    @Override
    public Output execute(Input input) {
        itemValidator.validateItemNameDuplication(input.name);
        Item item = itemWriterRepository.save(createNewItemFrom(input).toVo());
        return Output.of(item.toVo());
    }

    @Value(staticConstructor = "of")
    public static class Input implements UseCase.Input {
        String name;
        Long price;
        String description;
    }

    @Value(staticConstructor = "of")
    public static class Output implements UseCase.Output {
        ItemVo item;
    }

    private static Item createNewItemFrom(Input input) {
        return Item.create(input.getName(), input.getPrice(), input.getDescription());
    }
}
