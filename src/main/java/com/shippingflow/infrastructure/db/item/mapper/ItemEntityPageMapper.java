package com.shippingflow.infrastructure.db.item.mapper;

import com.shippingflow.core.domain.aggregate.item.dto.ItemWithStockDto;
import com.shippingflow.core.domain.common.pagination.PageResponse;
import com.shippingflow.infrastructure.db.item.entity.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemEntityPageMapper {

    public PageResponse<ItemWithStockDto> toItemWithStockDtoPageResponse(Page<ItemEntity> itemEntityPage) {
        return new PageResponse<>(
                toItemWithStockDtoList(itemEntityPage.getContent()),
                itemEntityPage.getNumber(),
                itemEntityPage.getSize(),
                itemEntityPage.getTotalElements(),
                itemEntityPage.getTotalPages()
        );
    }

    private static List<ItemWithStockDto> toItemWithStockDtoList(List<ItemEntity> itemEntities) {
        return itemEntities.stream()
                .map(ItemEntity::toItemWithStockDto)
                .toList();
    }
}
