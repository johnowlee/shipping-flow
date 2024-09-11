package com.shippingflow.core.usecase.stock;

import com.shippingflow.core.domain.stock.Stock;
import com.shippingflow.core.domain.stock.StockTransaction;
import com.shippingflow.core.domain.stock.repository.StockReaderRepository;
import com.shippingflow.core.domain.stock.repository.StockTransactionWriterRepository;
import com.shippingflow.core.domain.stock.repository.StockWriterRepository;
import com.shippingflow.core.exception.DomainException;
import com.shippingflow.core.exception.error.StockError;
import com.shippingflow.core.usecase.common.ClockManager;
import com.shippingflow.core.usecase.UseCase;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
public abstract class UpdateStockUseCase extends UseCase<UpdateStockUseCase.Input, UpdateStockUseCase.Output> {

    private final StockReaderRepository stockReaderRepository;
    private final StockWriterRepository stockWriterRepository;
    protected final StockTransactionWriterRepository stockTransactionWriterRepository;
    private final ClockManager clockManager;

    @Override
    public Output execute(Input input) {
        Output output = updateStockAndReturn(input);
        Stock stock = output.getStock();
        StockTransaction transaction = saveStockTransaction(stock, input.getQuantity(), clockManager);
        stock.addTransaction(transaction);
        return output;
    }

    private Output updateStockAndReturn(Input input) {
        return stockReaderRepository.findById(input.getId())
                .map(stock -> updateQuantity(stock, input.getQuantity()))
                .map(this::persist)
                .orElseThrow(() -> DomainException.from(StockError.NOT_FOUND_STOCK));
    }

    protected abstract Stock updateQuantity(Stock stock, long quantity);

    protected abstract StockTransaction saveStockTransaction(Stock stock, long quantity, ClockManager clockManager);

    @Value(staticConstructor = "of")
    public static class Input implements UseCase.Input {
        long id;
        long quantity;
    }

    @Value(staticConstructor = "of")
    public static class Output implements UseCase.Output {
        Stock stock;
    }

    private Output persist(Stock stock) {
        return Output.of(stockWriterRepository.update(stock));
    }

}
