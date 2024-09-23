package com.shippingflow.infrastructure.db.jpa.support.paging.mapper;

import com.shippingflow.core.domain.aggregate.item.dto.ItemWithStockDto;
import com.shippingflow.core.domain.common.pagination.PageResponse;
import com.shippingflow.infrastructure.db.jpa.item.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemEntityPageMapper {

    public PageResponse<ItemWithStockDto> toItemWithStockPageFrom(Page<ItemEntity> itemEntityPage) {
        return new PageResponse<>(
                toItemWithStockDtoList(itemEntityPage),
                itemEntityPage.getNumber(),
                itemEntityPage.getSize(),
                itemEntityPage.getTotalElements(),
                itemEntityPage.getTotalPages()
        );
    }

    private static List<ItemWithStockDto> toItemWithStockDtoList(Page<ItemEntity> itemEntityPage) {
        return itemEntityPage.getContent().stream()
                .map(ItemEntity::toItemWithStockDto)
                .toList();
    }
}
