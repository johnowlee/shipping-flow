package com.shippingflow.presenter.api;

import com.shippingflow.core.exception.DomainException;
import com.shippingflow.presenter.api.common.response.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ExceptionResponse handleCustomException(DomainException e) {
        return ExceptionResponse.from(e);
    }

    @ExceptionHandler(BindException.class)
    public ExceptionResponse bindException(BindException e) {
        return ExceptionResponse.from(e);
    }
}
