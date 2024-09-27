package com.shippingflow.presenter.api.common.response;

import com.shippingflow.core.exception.DomainException;
import com.shippingflow.core.exception.error.ErrorSpec;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;

import static org.springframework.http.HttpStatus.*;

public record ExceptionResponse(int code, HttpStatus status, String name, String message) {

    public static ExceptionResponse of(int code, HttpStatus status, String name, String message) {
        return new ExceptionResponse(code, status, name, message);
    }

    public static ExceptionResponse of(HttpStatus status, String name, String message) {
        return of(status.value(), status, name, message);
    }

    public static ExceptionResponse from(DomainException e) {
        return from(e.getErrorSpec());
    }

    public static ExceptionResponse from(BindException e) {
        return of(BAD_REQUEST, e.getFieldError().getField(), getBindingErrorMessage(e));
    }

    private static ExceptionResponse from(ErrorSpec errorSpec) {
        return of(INTERNAL_SERVER_ERROR,
                errorSpec.name(),
                errorSpec.getMessage()
        );
    }

    private static String getBindingErrorMessage(BindException e) {
        return e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
    }
}