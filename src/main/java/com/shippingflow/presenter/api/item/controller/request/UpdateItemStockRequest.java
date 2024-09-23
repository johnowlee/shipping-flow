package com.shippingflow.presenter.api.item.controller.request;

import com.shippingflow.core.domain.aggregate.item.model.local.StockTransactionType;
import com.shippingflow.presenter.api.item.controller.request.validator.ValidStockTransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record UpdateItemStockRequest(

        @NotBlank
        @ValidStockTransactionType
        String stockTransactionType,

        @Positive
        long quantity) {

        public StockTransactionType convertStockTransactionTypeToEnum() {
                return StockTransactionType.valueOf(this.stockTransactionType.toUpperCase());
        }
}
