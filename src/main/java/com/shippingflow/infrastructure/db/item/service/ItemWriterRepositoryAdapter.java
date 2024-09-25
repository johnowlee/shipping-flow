package com.shippingflow.infrastructure.db.item.service;

import com.shippingflow.core.domain.aggregate.item.dto.ItemSaveDto;
import com.shippingflow.core.domain.aggregate.item.dto.ItemWithStockDto;
import com.shippingflow.core.domain.aggregate.item.repository.ItemWriterRepository;
import com.shippingflow.infrastructure.db.item.entity.ItemEntity;
import com.shippingflow.infrastructure.db.item.port.ItemWriterPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ItemWriterRepositoryAdapter implements ItemWriterRepository {

    private final ItemWriterPort itemWriterPort;

    @Override
    public ItemWithStockDto saveNewItem(ItemSaveDto itemSaveDto) {
        ItemEntity itemEntity = ItemEntity.createFrom(itemSaveDto);
        return itemWriterPort.save(itemEntity).toItemWithStockDto();
    }

    @Override
    public ItemWithStockDto updateStock(ItemSaveDto itemSaveDto) {
        ItemEntity itemEntity = ItemEntity.buildFrom(itemSaveDto);
        return itemWriterPort.save(itemEntity).toItemWithStockDto();
    }
}
