package com.shippingflow.core.exception;

import com.shippingflow.core.exception.error.ErrorSpec;
import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {

    private final ErrorSpec errorSpec;

    private DomainException(ErrorSpec errorSpec) {
        super(errorSpec.getMessage());
        this.errorSpec = errorSpec;
    }

    public static DomainException from(ErrorSpec errorSpec) {
        return new DomainException(errorSpec);
    }
}
