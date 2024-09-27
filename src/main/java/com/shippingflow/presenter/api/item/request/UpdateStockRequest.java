package com.shippingflow.presenter.api.item.request;

import com.shippingflow.core.domain.aggregate.item.model.local.StockTransactionType;
import com.shippingflow.presenter.api.item.request.validator.ValidStockTransactionType;
import jakarta.validation.constraints.Positive;

public record UpdateStockRequest(

        @ValidStockTransactionType
        String stockTransactionType,

        @Positive
        long quantity) {

        public StockTransactionType convertStockTransactionTypeToEnum() {
                return StockTransactionType.valueOf(this.stockTransactionType.toUpperCase());
        }
}
