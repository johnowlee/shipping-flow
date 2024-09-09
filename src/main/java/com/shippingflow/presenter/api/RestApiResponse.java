package com.shippingflow.presenter.api;

import org.springframework.http.HttpStatus;

public record RestApiResponse<T> (int code, HttpStatus status, String message, T data) {

    public static <T> RestApiResponse<T> of(HttpStatus status, String message, T data) {
        return new RestApiResponse<>(status.value(), status, message, data);
    }

    public static <T> RestApiResponse<T> of(HttpStatus status, T data) {
        return of(status, status.name(), data);
    }

    public static <T> RestApiResponse<T> ok(T data) {
        return of(HttpStatus.OK, HttpStatus.OK.name(), data);
    }

    public static <T> RestApiResponse<T> created(T data) {
        return of(HttpStatus.CREATED, HttpStatus.CREATED.name(), data);
    }
}
