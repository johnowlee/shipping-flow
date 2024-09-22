package com.shippingflow.core.usecase.aggregate.item;

import com.shippingflow.core.domain.aggregate.item.component.ItemReader;
import com.shippingflow.core.domain.aggregate.item.component.ItemWriter;
import com.shippingflow.core.domain.aggregate.item.model.local.StockTransactionType;
import com.shippingflow.core.domain.aggregate.item.model.root.Item;
import com.shippingflow.core.usecase.common.ClockManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class DecreaseStockUseCase extends UpdateStockUseCase {

    public DecreaseStockUseCase(ItemReader itemReader, ItemWriter itemWriter, ClockManager clockManager) {
        super(itemReader, itemWriter, clockManager, StockTransactionType.DECREASE);
    }

    @Override
    protected void updateStockQuantity(Item item, long quantity) {
        item.decreaseStock(quantity);
    }
}
