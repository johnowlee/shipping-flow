package com.shippingflow.core.usecase.item;

import com.shippingflow.core.domain.item.Item;
import com.shippingflow.core.domain.item.component.ItemValidator;
import com.shippingflow.core.domain.item.repository.ItemWriterRepository;
import com.shippingflow.core.domain.stock.Stock;
import com.shippingflow.core.domain.stock.repository.StockWriterRepository;
import com.shippingflow.core.usecase.UseCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CreateItemUseCase extends UseCase<CreateItemUseCase.Input, CreateItemUseCase.Output> {
    private final ItemValidator itemValidator;
    private final ItemWriterRepository itemWriterRepository;
    private final StockWriterRepository stockWriterRepository;
    @Override
    public Output execute(Input input) {
        itemValidator.validateItemNameDuplication(input.name);

        Item item = itemWriterRepository.save(createNewItemFrom(input));
        Stock stock = stockWriterRepository.save(Stock.createNewStock(item));
        item.assignStock(stock);
        return new Output(item);
    }

    @Getter
    @RequiredArgsConstructor
    public static class Input implements UseCase.Input {

        private final String name;
        private final Long price;
        private final String description;
    }
    @Getter
    @RequiredArgsConstructor
    public static class Output implements UseCase.Output {

        private final Item item;

    }
    private static Item createNewItemFrom(Input input) {
        return Item.createNewItem(input.getName(), input.getPrice(), input.getDescription());
    }
}
