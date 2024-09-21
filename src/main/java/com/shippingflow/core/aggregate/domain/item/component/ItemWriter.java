package com.shippingflow.core.aggregate.domain.item.component;

import com.shippingflow.core.aggregate.domain.item.dto.ItemWithStockDto;
import com.shippingflow.core.aggregate.domain.item.repository.ItemWriterRepository;
import com.shippingflow.core.aggregate.domain.item.model.root.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ItemWriter {

    private final ItemWriterRepository itemWriterRepository;

    public Item save(Item item) {
        ItemWithStockDto itemWithStockDto = itemWriterRepository.save(item.toItemAggregateDto());
        return Item.from(itemWithStockDto);
    }

    public Item updateStock(Item item) {
        ItemWithStockDto itemWithStockDto = itemWriterRepository.updateStock(item.toItemAggregateDto());
        return Item.from(itemWithStockDto);
    }
}
