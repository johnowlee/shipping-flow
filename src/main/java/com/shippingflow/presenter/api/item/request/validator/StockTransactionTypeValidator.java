package com.shippingflow.presenter.api.item.request.validator;

import com.shippingflow.core.domain.aggregate.item.model.local.StockTransactionType;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StockTransactionTypeValidator implements ConstraintValidator<ValidStockTransactionType, String> {
    @Override
    public void initialize(ValidStockTransactionType constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String stockTransactionType, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(stockTransactionType)) {
            return false;
        }
        try {
            StockTransactionType.valueOf(stockTransactionType.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
