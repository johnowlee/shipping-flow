package com.shippingflow.presenter.api.item.common.response;

public record PageResponse(int pageNumber, int pageSize, long totalElements, int totalPages) {
    public static PageResponse of(int pageNumber, int pageSize, long totalElements, int totalPages) {
        return new PageResponse(pageNumber, pageSize, totalElements, totalPages);
    }

    public static PageResponse from(com.shippingflow.core.domain.common.pagination.PageResponse dto) {
        return new PageResponse(dto.pageNumber(), dto.pageSize(), dto.totalElements(), dto.totalPages());
    }
}
