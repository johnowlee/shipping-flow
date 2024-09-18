package com.shippingflow.core.aggregate.domain.item.component;

import com.shippingflow.core.aggregate.domain.item.repository.ItemWriterRepository;
import com.shippingflow.core.aggregate.domain.item.root.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ItemWriter {

    private final ItemWriterRepository itemWriterRepository;

    public Item save(Item item) {
        return itemWriterRepository.save(item.toVo());
    }
}
