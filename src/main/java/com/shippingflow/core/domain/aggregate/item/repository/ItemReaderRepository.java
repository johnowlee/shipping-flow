package com.shippingflow.core.domain.aggregate.item.repository;

import com.shippingflow.core.domain.aggregate.item.dto.ItemWithStockDto;
import com.shippingflow.core.domain.aggregate.item.dto.StockTransactionDto;
import com.shippingflow.core.domain.common.pagination.BasicPaginationRequest;
import com.shippingflow.core.domain.common.pagination.PageResponse;
import com.shippingflow.core.domain.common.pagination.SortablePaginationRequest;

import java.util.Optional;

public interface ItemReaderRepository {
    boolean existsByName(String name);

    Optional<ItemWithStockDto> findItemWithStockById(long itemId);

    PageResponse<ItemWithStockDto> findAllItemsWithStock(SortablePaginationRequest sortablePaginationRequest);

    PageResponse<StockTransactionDto> findStockTransactionsByItemId(long itemId, BasicPaginationRequest paginationRequest);
}
