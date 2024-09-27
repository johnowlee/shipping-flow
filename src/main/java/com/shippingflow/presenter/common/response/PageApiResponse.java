package com.shippingflow.presenter.common.response;

public record PageApiResponse(int number, int size, long totalElements, int totalPages) {
    public static PageApiResponse of(int number, int size, long totalElements, int totalPages) {
        return new PageApiResponse(number, size, totalElements, totalPages);
    }
}
