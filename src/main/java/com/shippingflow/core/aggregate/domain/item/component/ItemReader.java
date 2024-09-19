package com.shippingflow.core.aggregate.domain.item.component;

import com.shippingflow.core.aggregate.domain.item.repository.ItemReaderRepository;
import com.shippingflow.core.aggregate.domain.item.root.Item;
import com.shippingflow.core.exception.DomainException;
import com.shippingflow.core.exception.error.ItemError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ItemReader {

    private final ItemReaderRepository itemReaderRepository;

    public boolean doesItemExistByName(String name) {
        return itemReaderRepository.existsByName(name);
    }

    public Item findItemById(long id) {
        return itemReaderRepository.findById(id)
                .orElseThrow(() -> DomainException.from(ItemError.NOT_FOUND_ITEM));
    }
}
