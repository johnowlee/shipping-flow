package com.shippingflow.infrastructure.db.item.port;

import com.shippingflow.infrastructure.db.item.entity.ItemEntity;

public interface ItemWriterPort {

    ItemEntity save(ItemEntity itemEntity);
}
