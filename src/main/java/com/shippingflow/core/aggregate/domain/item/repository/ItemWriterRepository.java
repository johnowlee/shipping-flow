package com.shippingflow.core.aggregate.domain.item.repository;

import com.shippingflow.core.aggregate.domain.item.root.Item;
import com.shippingflow.core.aggregate.vo.ItemVo;

public interface ItemWriterRepository {

    Item save(ItemVo itemVo);

    Item update(ItemVo itemVo);
}