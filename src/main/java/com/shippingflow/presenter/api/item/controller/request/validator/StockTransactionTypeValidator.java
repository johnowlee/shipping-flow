package com.shippingflow.presenter.api.item.controller.request.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static com.shippingflow.core.domain.aggregate.item.model.local.StockTransactionType.DECREASE;
import static com.shippingflow.core.domain.aggregate.item.model.local.StockTransactionType.INCREASE;

public class StockTransactionTypeValidator implements ConstraintValidator<ValidStockTransactionType, String> {
    @Override
    public void initialize(ValidStockTransactionType constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String stockTransactionType, ConstraintValidatorContext context) {
        String transactionTypeUpperCase = stockTransactionType.toUpperCase();
        return transactionTypeUpperCase.equals(INCREASE.name()) ||
                transactionTypeUpperCase.equals(DECREASE.name());
    }
}
