package com.shippingflow.core.usecase.aggregate.item;

import com.shippingflow.core.aggregate.domain.item.component.ItemValidator;
import com.shippingflow.core.aggregate.domain.item.component.ItemWriter;
import com.shippingflow.core.aggregate.domain.item.local.Stock;
import com.shippingflow.core.aggregate.domain.item.local.StockTransactionType;
import com.shippingflow.core.aggregate.domain.item.root.Item;
import com.shippingflow.core.aggregate.vo.ItemVo;
import com.shippingflow.core.usecase.UseCase;
import com.shippingflow.core.usecase.common.ClockManager;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CreateItemUseCase extends UseCase<CreateItemUseCase.Input, CreateItemUseCase.Output> {

    private final ItemValidator itemValidator;
    private final ItemWriter itemWriter;
    private final ClockManager clockManager;

    @Override
    public Output execute(Input input) {
        itemValidator.validateItemNameDuplication(input.name);
        Item item = itemWriter.save(createItemFrom(input));
        return Output.of(item.toVo());
    }

    @Value(staticConstructor = "of")
    public static class Input implements UseCase.Input {
        String name;
        Long price;
        String description;
        Long quantity;
    }

    @Value(staticConstructor = "of")
    public static class Output implements UseCase.Output {
        // TODO: DTO로 변경. VO 삭제?
        ItemVo item;
    }

    private Item createItemFrom(Input input) {
        Item item = Item.create(input.getName(), input.getPrice(), input.getDescription());
        if (input.getQuantity() != null) {
            setStockAndRecordTransaction(item, input.getQuantity());
        }
        return item;
    }

    private void setStockAndRecordTransaction(Item item, long quantity) {
        Stock stock = Stock.create(quantity);
        item.bind(stock);
        item.recordStockTransaction(StockTransactionType.INCREASE, quantity, clockManager.getNowDateTime());
    }
}
