package com.shippingflow.infrastructure.service.item;

import com.shippingflow.core.domain.aggregate.item.dto.ItemWithStockDto;
import com.shippingflow.core.domain.common.pagination.PageResponse;
import com.shippingflow.infrastructure.db.item.jpa.entity.ItemEntity;
import com.shippingflow.infrastructure.db.item.jpa.entity.StockEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class ItemEntityPageMapperTest {

    @DisplayName("ItemEntity 타입의 Page 객체를 ItemWithStockDto 타입의 PageResponse로 매핑한다.")
    @Test
    void toItemWithStockDtoPageResponse() {
        // given
        ItemEntity itemEntity1 = createItemEntity("ItemA", 1000L, "Description A", 5000L);
        ItemEntity itemEntity2 = createItemEntity("ItemB", 2000L, "Description B", 3000L);

        List<ItemEntity> itemEntityList = List.of(itemEntity1, itemEntity2);
        Pageable pageable = PageRequest.of(0, 5);
        Page<ItemEntity> itemEntityPage = new PageImpl<>(itemEntityList, pageable, itemEntityList.size());

        // when
        ItemEntityPageMapper itemEntityPageMapper = new ItemEntityPageMapper();
        PageResponse<ItemWithStockDto> actual = itemEntityPageMapper.toItemWithStockDtoPageResponse(itemEntityPage);

        // then
        assertThat(actual.pageNumber()).isEqualTo(0);
        assertThat(actual.pageSize()).isEqualTo(5);
        assertThat(actual.totalElements()).isEqualTo(2);
        assertThat(actual.totalPages()).isEqualTo(1);

        assertThat(actual.content())
                .hasSize(2)
                .extracting(
                        itemWithStockDto -> itemWithStockDto.item().name(),
                        itemWithStockDto -> itemWithStockDto.item().price(),
                        itemWithStockDto -> itemWithStockDto.item().description(),
                        itemWithStockDto -> itemWithStockDto.stock().quantity()
                )
                .containsExactlyInAnyOrder(
                        tuple("ItemA", 1000L, "Description A", 5000L),
                        tuple("ItemB", 2000L, "Description B", 3000L)
                );
    }

    private ItemEntity createItemEntity(String name, Long price, String description, Long stockQuantity) {
        ItemEntity itemEntity = ItemEntity.builder()
                .name(name)
                .price(price)
                .description(description)
                .build();

        StockEntity stockEntity = StockEntity.builder()
                .quantity(stockQuantity)
                .build();
        itemEntity.bind(stockEntity);

        return itemEntity;
    }
}