package com.shippingflow.core.domain.common.pagination;

public record PaginationRequest(int page, int size, String sortBy, String sortDir) {
    public static PaginationRequest of(int page, int size, String sortBy, String sortDir) {
        return new PaginationRequest(page - 1, size, sortBy, sortDir);
    }
}
