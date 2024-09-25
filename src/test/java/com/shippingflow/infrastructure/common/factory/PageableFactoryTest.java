package com.shippingflow.infrastructure.common.factory;

import com.shippingflow.core.domain.common.pagination.SortablePaginationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

class PageableFactoryTest {

    @DisplayName("정렬 조건 없이 Pageable을 생성한다.")
    @Test
    void createPageableWithoutSort() {
        // given
        SortablePaginationRequest sortablePaginationRequest = new SortablePaginationRequest(1, 10, null, null);

        // when
        PageableFactory pageableFactory = new PageableFactory();
        Pageable pageable = pageableFactory.createPageable(sortablePaginationRequest);

        // then
        assertThat(pageable).isInstanceOf(PageRequest.class);
        assertThat(pageable.getPageNumber()).isEqualTo(1);
        assertThat(pageable.getPageSize()).isEqualTo(10);
        assertThat(pageable.getSort().isSorted()).isFalse();
    }

    @DisplayName("오름차순 정렬 조건을 적용하여 Pageable을 생성한다.")
    @Test
    void createPageableWithSortAscending() {
        // given
        SortablePaginationRequest sortablePaginationRequest = new SortablePaginationRequest(0, 5, "name", "asc");

        // when
        PageableFactory pageableFactory = new PageableFactory();
        Pageable pageable = pageableFactory.createPageable(sortablePaginationRequest);

        // then
        assertThat(pageable).isInstanceOf(PageRequest.class);
        assertThat(pageable.getPageNumber()).isEqualTo(0);
        assertThat(pageable.getPageSize()).isEqualTo(5);
        assertThat(pageable.getSort().isSorted()).isTrue();
        assertThat(pageable.getSort().getOrderFor("name").getDirection()).isEqualTo(Sort.Direction.ASC);
    }

    @DisplayName("내림차순 정렬 조건을 적용하여 Pageable을 생성한다.")
    @Test
    void createPageableWithSortDescending() {
        // given
        SortablePaginationRequest sortablePaginationRequest = new SortablePaginationRequest(2, 20, "price", "desc");

        // when
        PageableFactory pageableFactory = new PageableFactory();
        Pageable pageable = pageableFactory.createPageable(sortablePaginationRequest);

        // then
        assertThat(pageable).isInstanceOf(PageRequest.class);
        assertThat(pageable.getPageNumber()).isEqualTo(2);
        assertThat(pageable.getPageSize()).isEqualTo(20);
        assertThat(pageable.getSort().isSorted()).isTrue();
        assertThat(pageable.getSort().getOrderFor("price").getDirection()).isEqualTo(Sort.Direction.DESC);
    }
}