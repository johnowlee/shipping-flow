package com.shippingflow.core.usecase.aggregate.item;

import com.shippingflow.core.domain.aggregate.item.component.ItemReader;
import com.shippingflow.core.domain.aggregate.item.dto.ItemWithStockDto;
import com.shippingflow.core.domain.common.pagination.PaginationRequest;
import com.shippingflow.core.domain.common.pagination.PageResponse;
import com.shippingflow.core.usecase.UseCase;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class GetItemsUseCase extends UseCase<GetItemsUseCase.Input, GetItemsUseCase.Output> {

    private final ItemReader itemReader;

    @Override
    public Output execute(Input input) {
        PageResponse<ItemWithStockDto> pageResponse = itemReader.getItems(input.paginationRequest);
        return Output.of(pageResponse);
    }

    @Value(staticConstructor = "of")
    public static class Input implements UseCase.Input {
        PaginationRequest paginationRequest;
    }

    @Value(staticConstructor = "of")
    public static class Output implements UseCase.Output {
        PageResponse<ItemWithStockDto> pageResponse;
    }
}
