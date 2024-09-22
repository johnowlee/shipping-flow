package com.shippingflow.presenter.api.item.controller.request;

import com.shippingflow.core.domain.aggregate.item.model.local.StockTransactionType;
import com.shippingflow.presenter.api.item.controller.request.validator.ValidStockTransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import static com.shippingflow.core.domain.aggregate.item.model.local.StockTransactionType.*;

public record UpdateItemStockRequest(

        @NotBlank
        @ValidStockTransactionType
        String transactionType,

        @Positive
        long quantity) {

        public StockTransactionType convertTransactionTypeToEnum() {
                String transactionTypeUpperCase = this.transactionType.toUpperCase();
                return transactionTypeUpperCase.equals(INCREASE.name()) ? INCREASE : DECREASE;
        }
}
