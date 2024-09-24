package com.shippingflow.infrastructure.db.item.jpa.repository;

import com.shippingflow.core.domain.aggregate.item.dto.ItemWithStockDto;
import com.shippingflow.core.domain.aggregate.item.repository.ItemReaderRepository;
import com.shippingflow.core.domain.common.pagination.PageResponse;
import com.shippingflow.core.domain.common.pagination.PaginationRequest;
import com.shippingflow.infrastructure.db.item.jpa.entity.ItemEntity;
import com.shippingflow.infrastructure.service.item.ItemEntityPageMapper;
import com.shippingflow.infrastructure.service.support.paging.PageableFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

// TODO: 불필요클래스. 삭제 요망
@RequiredArgsConstructor
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
        return itemEntityPageMapper.toItemWithStockDtoPageResponse(itemEntityPage);
    }
}
