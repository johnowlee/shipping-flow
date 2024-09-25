package com.shippingflow.infrastructure.common.factory;

import com.shippingflow.core.domain.common.pagination.SortablePaginationRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PageableFactory {

    public Pageable createPageable(SortablePaginationRequest sortablePaginationRequest) {
        return isSortable(sortablePaginationRequest) ? createPageRequestWithSort(sortablePaginationRequest) : createPageRequest(sortablePaginationRequest);
    }

    private boolean isSortable(SortablePaginationRequest sortablePaginationRequest) {
        return sortablePaginationRequest.sortBy() != null;
    }

    private PageRequest createPageRequestWithSort(SortablePaginationRequest sortablePaginationRequest) {
        String sortBy = sortablePaginationRequest.sortBy();
        String sortDir = sortablePaginationRequest.sortDir();
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return PageRequest.of(sortablePaginationRequest.page(), sortablePaginationRequest.size(), sort);
    }

    private PageRequest createPageRequest(SortablePaginationRequest sortablePaginationRequest) {
        return PageRequest.of(sortablePaginationRequest.page(), sortablePaginationRequest.size());
    }
}
