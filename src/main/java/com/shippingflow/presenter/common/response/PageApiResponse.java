package com.shippingflow.presenter.common.response;

public record PageApiResponse(int page, int size, long totalElements, int totalPages) {
    public static PageApiResponse of(int page, int size, long totalElements, int totalPages) {
        return new PageApiResponse(page, size, totalElements, totalPages);
    }
}
