package com.shippingflow.core.usecase;

import com.shippingflow.core.domain.Item;
import com.shippingflow.core.domain.ItemReaderRepository;
import com.shippingflow.core.domain.ItemWriterRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreateItemUseCase extends UseCase<CreateItemUseCase.Input, CreateItemUseCase.Output> {
    private final ItemWriterRepository itemWriterRepository;
    private final ItemReaderRepository itemReaderRepository;
    @Override
    public Output execute(Input input) {
        if (itemReaderRepository.existsByName(input.getName())) {
            throw new IllegalStateException("이미 존재하는 상품명 입니다.");
        }

        Item newItem = Item.createNewItem(input.getName(), input.getDescription());
        Item persistedItem = itemWriterRepository.save(newItem);
        return new Output(persistedItem);
    }

    @Getter
    @RequiredArgsConstructor
    public static class Input implements UseCase.Input {
        private final String name;
        private final String description;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Output implements UseCase.Output {
        private final Item item;
    }

}
