package com.shippingflow.core.exception.error;

public interface ErrorSpec {
    String name();

    RuntimeException getExceptionType();

    String getMessage();
}
