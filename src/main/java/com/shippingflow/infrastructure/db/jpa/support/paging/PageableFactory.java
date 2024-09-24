package com.shippingflow.infrastructure.db.jpa.support.paging;

import com.shippingflow.core.domain.common.pagination.PaginationRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PageableFactory {

    public Pageable createPageable(PaginationRequest paginationRequest) {
        return isSortable(paginationRequest) ? createPageRequestWithSort(paginationRequest) : createPageRequest(paginationRequest);
    }

    private boolean isSortable(PaginationRequest paginationRequest) {
        return paginationRequest.sortBy() != null;
    }

    private PageRequest createPageRequestWithSort(PaginationRequest paginationRequest) {
        String sortBy = paginationRequest.sortBy();
        String sortDir = paginationRequest.sortDir();
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return PageRequest.of(paginationRequest.page(), paginationRequest.size(), sort);
    }

    private PageRequest createPageRequest(PaginationRequest paginationRequest) {
        return PageRequest.of(paginationRequest.page(), paginationRequest.size());
    }
}
