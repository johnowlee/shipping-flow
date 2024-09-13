package com.shippingflow.core.usecase.aggregate.item;

import com.shippingflow.core.aggregate.item.local.StockTransactionType;
import com.shippingflow.core.aggregate.item.repository.ItemReaderRepository;
import com.shippingflow.core.aggregate.item.repository.ItemWriterRepository;
import com.shippingflow.core.aggregate.item.root.Item;
import com.shippingflow.core.usecase.common.ClockManager;
import org.springframework.stereotype.Service;

@Service
public class DecreaseStockUseCase extends UpdateStockUseCase {


    public DecreaseStockUseCase(
            ItemReaderRepository itemReaderRepository,
            ItemWriterRepository itemWriterRepository,
            ClockManager clockManager) {
        super(itemReaderRepository, itemWriterRepository, clockManager);
    }

    @Override
    protected Item updateQuantity(Item item, long quantity) {
        item.decreaseStock(quantity);
        return item;
    }

    @Override
    protected Item addStockTransaction(Item item, long quantity, ClockManager clockManager) {
        item.addStockTransaction(StockTransactionType.DECREASE, quantity, clockManager.getNowDateTime());
        return item;
    }
}
