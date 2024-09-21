package com.shippingflow.core.usecase.aggregate.item;

import com.shippingflow.core.aggregate.domain.item.component.ItemReader;
import com.shippingflow.core.aggregate.domain.item.component.ItemWriter;
import com.shippingflow.core.aggregate.domain.item.local.StockTransactionType;
import com.shippingflow.core.aggregate.domain.item.root.Item;
import com.shippingflow.core.usecase.common.ClockManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class DecreaseStockUseCase extends UpdateStockUseCase {

    public DecreaseStockUseCase(ItemReader itemReader, ItemWriter itemWriter, ClockManager clockManager) {
        super(itemReader, itemWriter, clockManager);
    }

    @Override
    protected void updateStockQuantity(Item item, long quantity) {
        item.decreaseStock(quantity);
    }

    @Override
    protected void recordStockTransaction(Item item, long quantity, ClockManager clockManager) {
        item.recordStockTransaction(StockTransactionType.DECREASE, quantity, clockManager.getNowDateTime());
    }
}
