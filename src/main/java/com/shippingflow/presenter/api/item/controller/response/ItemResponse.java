package com.shippingflow.presenter.api.item.controller.response;

import com.shippingflow.core.usecase.aggregate.item.CreateItemUseCase;
import com.shippingflow.core.aggregate.vo.ItemVo;

public record ItemResponse(long id, String name, Long price, String description) {

    public static ItemResponse from(CreateItemUseCase.Output output) {
        ItemVo itemVo = output.getItem();
        return new ItemResponse(itemVo.id(), itemVo.name(), itemVo.price(), itemVo.description());
    }
}
