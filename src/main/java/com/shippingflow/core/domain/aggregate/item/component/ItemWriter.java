package com.shippingflow.core.domain.aggregate.item.component;

import com.shippingflow.core.domain.aggregate.item.dto.ItemWithStockDto;
import com.shippingflow.core.domain.aggregate.item.repository.ItemWriterRepository;
import com.shippingflow.core.domain.aggregate.item.model.root.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ItemWriter {

    private final ItemWriterRepository itemWriterRepository;

    public Item saveNewItem(Item item) {
        ItemWithStockDto itemWithStockDto = itemWriterRepository.saveNewItem(item.toItemSaveDto());
        return Item.from(itemWithStockDto);
    }

    public Item updateStock(Item item) {
        ItemWithStockDto itemWithStockDto = itemWriterRepository.updateStock(item.toItemSaveDto());
        return Item.from(itemWithStockDto);
    }
}
