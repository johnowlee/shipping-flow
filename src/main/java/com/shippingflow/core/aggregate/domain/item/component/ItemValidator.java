package com.shippingflow.core.aggregate.domain.item.component;

import com.shippingflow.core.exception.DomainException;
import com.shippingflow.core.exception.error.ItemError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ItemValidator {

    private final ItemReader itemReader;

    public void validateItemNameDuplication(String name) {
        if (itemReader.doesItemExistByName(name)) {
            throw DomainException.from(ItemError.ITEM_NAME_ALREADY_EXISTS);
        }
    }
}
