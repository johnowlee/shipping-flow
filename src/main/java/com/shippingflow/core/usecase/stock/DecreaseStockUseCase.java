package com.shippingflow.core.usecase.stock;

import com.shippingflow.core.domain.stock.Stock;
import com.shippingflow.core.domain.stock.StockTransaction;
import com.shippingflow.core.domain.stock.StockTransactionType;
import com.shippingflow.core.domain.stock.repository.StockReaderRepository;
import com.shippingflow.core.domain.stock.repository.StockTransactionWriterRepository;
import com.shippingflow.core.domain.stock.repository.StockWriterRepository;
import com.shippingflow.core.usecase.common.ClockManager;
import org.springframework.stereotype.Service;

@Service
public class DecreaseStockUseCase extends UpdateStockUseCase {

    public DecreaseStockUseCase(
            StockReaderRepository stockReaderRepository,
            StockWriterRepository stockWriterRepository,
            StockTransactionWriterRepository stockTransactionWriterRepository,
            ClockManager clockManager) {
        super(stockReaderRepository, stockWriterRepository, stockTransactionWriterRepository, clockManager);
    }

    @Override
    protected Stock updateQuantity(Stock stock, long quantity) {
        stock.decrease(quantity);
        return stock;
    }

    @Override
    protected StockTransaction saveStockTransaction(Stock stock, long quantity, ClockManager clockManager) {
        StockTransaction newStockTransaction = StockTransaction.createNewStockTransaction(stock, quantity, StockTransactionType.DECREASE, clockManager.getNowDateTime());
        return stockTransactionWriterRepository.save(newStockTransaction);
    }
}
