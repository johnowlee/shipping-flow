package com.shippingflow.core.usecase.aggregate.item;

import com.shippingflow.core.domain.aggregate.item.local.StockTransactionType;
import com.shippingflow.core.domain.aggregate.item.repository.ItemReaderRepository;
import com.shippingflow.core.domain.aggregate.item.repository.ItemWriterRepository;
import com.shippingflow.core.domain.aggregate.item.root.Item;
import com.shippingflow.core.usecase.common.ClockManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class IncreaseStockUseCase extends UpdateStockUseCase {


    public IncreaseStockUseCase(
            ItemReaderRepository itemReaderRepository,
            ItemWriterRepository itemWriterRepository,
            ClockManager clockManager) {
        super(itemReaderRepository, itemWriterRepository, clockManager);
    }

    @Override
    protected Item updateStockQuantity(Item item, long quantity) {
        item.increaseStock(quantity);
        return item;
    }

    @Override
    protected Item recordStockTransaction(Item item, long quantity, ClockManager clockManager) {
        item.recordStockTransaction(StockTransactionType.INCREASE, quantity, clockManager.getNowDateTime());
        return item;
    }
}
