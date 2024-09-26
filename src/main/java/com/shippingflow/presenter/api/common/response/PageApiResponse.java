package com.shippingflow.presenter.api.common.response;

public record PageApiResponse(int pageNumber, int pageSize, long totalElements, int totalPages) {
    public static PageApiResponse of(int pageNumber, int pageSize, long totalElements, int totalPages) {
        return new PageApiResponse(pageNumber, pageSize, totalElements, totalPages);
    }
}
