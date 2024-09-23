package com.shippingflow.infrastructure.db.jpa.item.repository;

import com.shippingflow.core.domain.aggregate.item.dto.ItemWithStockDto;
import com.shippingflow.core.domain.aggregate.item.repository.ItemReaderRepository;
import com.shippingflow.core.domain.common.pagination.PageResponse;
import com.shippingflow.core.domain.common.pagination.PaginationRequest;
import com.shippingflow.infrastructure.db.jpa.item.ItemEntity;
import com.shippingflow.infrastructure.db.jpa.support.paging.PageableFactory;
import com.shippingflow.infrastructure.db.jpa.support.paging.mapper.ItemEntityPageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ItemReaderJpaRepository implements ItemReaderRepository {

    private final ItemJpaRepository itemJpaRepository;
    private final ItemEntityPageMapper itemEntityPageMapper;
    private final PageableFactory pageableFactory;

    @Override
    public boolean existsByName(String name) {
        return itemJpaRepository.existsByName(name);
    }

    @Override
    public Optional<ItemWithStockDto> findItemWithStockById(long itemId) {
       return itemJpaRepository.findById(itemId)
               .map(ItemEntity::toItemWithStockDto);
    }

    @Override
    public PageResponse<ItemWithStockDto> findAllItemsWithStock(PaginationRequest paginationRequest) {
        // TODO: 매핑과 같은 역할을 Repository에서 하는게 맞는가? SRP위배
        Pageable pageable = pageableFactory.createPageable(paginationRequest);
        Page<ItemEntity> itemEntityPage = itemJpaRepository.findAll(pageable);
        return itemEntityPageMapper.toItemWithStockPageFrom(itemEntityPage);
    }
}
