package com.shippingflow.infrastructure.service.item;

import com.shippingflow.core.domain.aggregate.item.dto.ItemWithStockDto;
import com.shippingflow.core.domain.aggregate.item.repository.ItemReaderRepository;
import com.shippingflow.core.domain.common.pagination.PageResponse;
import com.shippingflow.core.domain.common.pagination.PaginationRequest;
import com.shippingflow.infrastructure.db.item.jpa.entity.ItemEntity;
import com.shippingflow.infrastructure.db.item.port.ItemReaderPort;
import com.shippingflow.infrastructure.service.support.paging.PageableFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

// TODO: Test
@RequiredArgsConstructor
@Service
public class ItemReaderRepositoryHelper implements ItemReaderRepository {

    private final ItemReaderPort itemReaderPort;
    private final ItemEntityPageMapper itemEntityPageMapper;
    private final PageableFactory pageableFactory;

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
    public PageResponse<ItemWithStockDto> findAllItemsWithStock(PaginationRequest paginationRequest) {
        Pageable pageable = pageableFactory.createPageable(paginationRequest);
        Page<ItemEntity> itemEntityPage = itemReaderPort.findAllItems(pageable);
        return itemEntityPageMapper.toItemWithStockPageFrom(itemEntityPage);
    }
}
