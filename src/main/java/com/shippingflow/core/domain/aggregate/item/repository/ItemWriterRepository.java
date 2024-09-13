package com.shippingflow.core.domain.aggregate.item.repository;

import com.shippingflow.core.domain.aggregate.item.root.Item;
import com.shippingflow.core.usecase.aggregate.item.vo.ItemVo;

public interface ItemWriterRepository {

    Item save(ItemVo itemVo);

    Item update(ItemVo itemVo);
}