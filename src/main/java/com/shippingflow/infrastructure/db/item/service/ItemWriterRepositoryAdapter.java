package com.shippingflow.infrastructure.db.item.service;

import com.shippingflow.core.domain.aggregate.item.dto.ItemSaveDto;
import com.shippingflow.core.domain.aggregate.item.dto.ItemWithStockDto;
import com.shippingflow.core.domain.aggregate.item.repository.ItemWriterRepository;
import com.shippingflow.infrastructure.db.item.entity.ItemEntity;
import com.shippingflow.infrastructure.db.item.port.ItemReaderPort;
import com.shippingflow.infrastructure.db.item.port.ItemWriterPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ItemWriterRepositoryAdapter implements ItemWriterRepository {

    private final ItemWriterPort itemWriterPort;
    private final ItemReaderPort itemReaderPort;

    @Override
    public ItemWithStockDto saveNewItem(ItemSaveDto itemSaveDto) {
        ItemEntity itemEntity = ItemEntity.createFrom(itemSaveDto);
        return itemWriterPort.save(itemEntity).toItemWithStockDto();
    }

    public ItemWithStockDto updateStock(ItemSaveDto itemSaveDto) {
        ItemEntity itemEntity = itemReaderPort.findItemById(itemSaveDto.item().id())
                .orElseThrow(EntityNotFoundException::new);
        itemEntity.updateStockFrom(itemSaveDto);
        return itemWriterPort.save(itemEntity).toItemWithStockDto();
    }
}
