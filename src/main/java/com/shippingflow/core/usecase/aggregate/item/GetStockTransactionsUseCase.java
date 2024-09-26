package com.shippingflow.core.usecase.aggregate.item;

import com.shippingflow.core.domain.aggregate.item.component.ItemReader;
import com.shippingflow.core.domain.aggregate.item.dto.StockTransactionDto;
import com.shippingflow.core.domain.common.pagination.BasicPaginationRequest;
import com.shippingflow.core.domain.common.pagination.PageResponse;
import com.shippingflow.core.usecase.UseCase;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class GetStockTransactionsUseCase extends UseCase<GetStockTransactionsUseCase.Input, GetStockTransactionsUseCase.Output> {

    private final ItemReader itemReader;

    // TODO: 2024-09-26 test
    @Override
    public Output execute(Input input) {
        PageResponse<StockTransactionDto> stockTransactions = itemReader.getStockTransactions(input.itemId, input.paginationRequest);
        return Output.of(stockTransactions);
    }

    @Value(staticConstructor = "of")
    public static class Input implements UseCase.Input {
        long itemId;
        BasicPaginationRequest paginationRequest;
    }

    @Value(staticConstructor = "of")
    public static class Output implements UseCase.Output {
        PageResponse<StockTransactionDto> pageResponse;
    }

}
