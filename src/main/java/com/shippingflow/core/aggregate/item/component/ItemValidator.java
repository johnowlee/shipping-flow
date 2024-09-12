package com.shippingflow.core.aggregate.item.component;

import com.shippingflow.core.aggregate.item.repository.ItemReaderRepository;
import com.shippingflow.core.exception.DomainException;
import com.shippingflow.core.exception.error.ItemError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ItemValidator {

    private final ItemReaderRepository itemReaderRepository;

    public void validateItemNameDuplication(String name) {
        if (itemReaderRepository.existsByName(name)) {
            throw DomainException.from(ItemError.ITEM_NAME_ALREADY_EXISTS);
        }
    }
}
