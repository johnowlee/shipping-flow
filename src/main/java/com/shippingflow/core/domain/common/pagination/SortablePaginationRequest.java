package com.shippingflow.core.domain.common.pagination;

public record SortablePaginationRequest(int page, int size, String sortBy, String sortDir) {
    public static SortablePaginationRequest of(int page, int size, String sortBy, String sortDir) {
        return new SortablePaginationRequest(page - 1, size, sortBy, sortDir);
    }
}
