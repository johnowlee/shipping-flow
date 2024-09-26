package com.shippingflow.core.domain.aggregate.item.component;

import com.shippingflow.core.domain.aggregate.item.dto.StockTransactionDto;
import com.shippingflow.core.domain.aggregate.item.repository.ItemReaderRepository;
import com.shippingflow.core.domain.aggregate.item.dto.ItemWithStockDto;
import com.shippingflow.core.domain.aggregate.item.model.root.Item;
import com.shippingflow.core.domain.common.pagination.BasicPaginationRequest;
import com.shippingflow.core.domain.common.pagination.SortablePaginationRequest;
import com.shippingflow.core.domain.common.pagination.PageResponse;
import com.shippingflow.core.exception.DomainException;
import com.shippingflow.core.exception.error.ItemError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ItemReader {

    private final ItemReaderRepository itemReaderRepository;

    public boolean doesItemExistByName(String name) {
        return itemReaderRepository.existsByName(name);
    }

    public Item getItemWithStockById(long id) {
        ItemWithStockDto dto = itemReaderRepository.findItemWithStockById(id)
                .orElseThrow(() -> DomainException.from(ItemError.NOT_FOUND_ITEM));
        return Item.from(dto);
    }

    public PageResponse<ItemWithStockDto> getItems(SortablePaginationRequest sortablePaginationRequest) {
        return itemReaderRepository.findAllItemsWithStock(sortablePaginationRequest);
    }

    // TODO: 2024-09-26 test
    public PageResponse<StockTransactionDto> getStockTransactions(long itemId, BasicPaginationRequest paginationRequest) {
        return itemReaderRepository.findStockTransactionsByItemId(itemId, paginationRequest);
    }
}
