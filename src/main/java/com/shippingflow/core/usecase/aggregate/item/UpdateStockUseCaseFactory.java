package com.shippingflow.core.usecase.aggregate.item;

import com.shippingflow.core.domain.aggregate.item.model.local.StockTransactionType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UpdateStockUseCaseFactory {

    private final Map<StockTransactionType, UpdateStockUseCase> strategies = new HashMap<>();

    public UpdateStockUseCaseFactory(List<UpdateStockUseCase> useCases) {
        useCases.forEach(useCase -> {
            StockTransactionType type = useCase.getTransactionType();
            strategies.put(type, useCase);
        });
    }

    public UpdateStockUseCase getUseCaseBy(StockTransactionType transactionType) {
        return strategies.get(transactionType);
    }
}
