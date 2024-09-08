package com.shippingflow.core.domain.item.repository;

import com.shippingflow.core.domain.item.Item;

public interface ItemWriterRepository {

    Item save(Item item);
}
