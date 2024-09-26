package com.shippingflow.infrastructure.db.item.service;

import com.shippingflow.core.domain.aggregate.item.dto.ItemWithStockDto;
import com.shippingflow.core.domain.aggregate.item.dto.StockTransactionDto;
import com.shippingflow.core.domain.aggregate.item.repository.ItemReaderRepository;
import com.shippingflow.core.domain.common.pagination.BasicPaginationRequest;
import com.shippingflow.core.domain.common.pagination.PageResponse;
import com.shippingflow.core.domain.common.pagination.SortablePaginationRequest;
import com.shippingflow.infrastructure.common.factory.PageableFactory;
import com.shippingflow.infrastructure.db.item.entity.ItemEntity;
import com.shippingflow.infrastructure.db.item.entity.StockTransactionEntity;
import com.shippingflow.infrastructure.db.item.mapper.ItemEntityPageMapper;
import com.shippingflow.infrastructure.db.item.mapper.StockTransactionEntityPageMapper;
import com.shippingflow.infrastructure.db.item.port.ItemReaderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ItemReaderRepositoryAdapter implements ItemReaderRepository {

    private final ItemReaderPort itemReaderPort;

    private final PageableFactory pageableFactory;
    private final ItemEntityPageMapper itemEntityPageMapper;
    private final StockTransactionEntityPageMapper stockTransactionEntityPageMapper;

    @Override
    public boolean existsByName(String name) {
        return itemReaderPort.existsByName(name);
    }

    @Override
    public Optional<ItemWithStockDto> findItemWithStockById(long itemId) {
        return itemReaderPort.findItemById(itemId)
                .map(ItemEntity::toItemWithStockDto);
    }

    @Override
    public PageResponse<ItemWithStockDto> findAllItemsWithStock(SortablePaginationRequest sortablePaginationRequest) {
        Pageable pageable = pageableFactory.createPageable(sortablePaginationRequest);
        Page<ItemEntity> itemEntityPage = itemReaderPort.findAllItems(pageable);
        return itemEntityPageMapper.toItemWithStockDtoPageResponse(itemEntityPage);
    }

    @Override
    public PageResponse<StockTransactionDto> findStockTransactionsByItemId(long itemId, BasicPaginationRequest paginationRequest) {
        Pageable pageable = pageableFactory.createPageable(paginationRequest);
        Page<StockTransactionEntity> stockTransactionEntityPage = itemReaderPort.findAllStockTransactionsByItemId(itemId, pageable);
        return stockTransactionEntityPageMapper.toStockTransactionDtoPageResponse(stockTransactionEntityPage);
    }
}
