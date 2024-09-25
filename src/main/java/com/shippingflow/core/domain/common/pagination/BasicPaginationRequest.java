package com.shippingflow.core.domain.common.pagination;

public record BasicPaginationRequest(int page, int size) {
    public static BasicPaginationRequest of(int page, int size) {
        return new BasicPaginationRequest(page - 1, size);
    }
}
