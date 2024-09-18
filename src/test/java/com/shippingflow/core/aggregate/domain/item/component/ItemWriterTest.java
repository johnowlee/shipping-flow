package com.shippingflow.core.aggregate.domain.item.component;

import com.shippingflow.core.aggregate.domain.item.repository.ItemWriterRepository;
import com.shippingflow.core.aggregate.domain.item.root.Item;
import com.shippingflow.core.aggregate.vo.ItemVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ItemWriterTest {

    @Mock
    ItemWriterRepository itemWriterRepository;

    @InjectMocks
    ItemWriter itemWriter;

    @DisplayName("상품을 저장한다.")
    @Test
    void save() {
        // given
        Item item = Item.builder()
                .id(1L)
                .name("itemA")
                .price(1000L)
                .build();

        given(itemWriterRepository.save(any(ItemVo.class))).willReturn(item);

        // when
        Item actual = itemWriter.save(item);

        // then
        assertThat(actual.getId()).isEqualTo(1L);
        assertThat(actual.getName()).isEqualTo("itemA");
        assertThat(actual.getPrice()).isEqualTo(1000L);
    }

    @DisplayName("상품을 수정한다.")
    @Test
    void update() {
        // given
        Item item = Item.builder()
                .id(1L)
                .name("itemA")
                .price(1000L)
                .build();

        Item updatingItem = Item.builder()
                .id(item.getId())
                .name("itemB")
                .price(1500L)
                .build();

        given(itemWriterRepository.update(updatingItem.toVo())).willReturn(updatingItem);

        // when
        Item actual = itemWriter.update(updatingItem);

        // then
        assertThat(actual.getId()).isEqualTo(1L);
        assertThat(actual.getName()).isEqualTo("itemB");
        assertThat(actual.getPrice()).isEqualTo(1500L);
    }
}