package com.shippingflow.core.usecase;

import com.shippingflow.core.domain.Item;
import com.shippingflow.core.domain.ItemReaderRepository;
import com.shippingflow.core.domain.ItemWriterRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CreateItemUseCaseTest {

    @Mock
    ItemWriterRepository itemWriterRepository;

    @Mock
    ItemReaderRepository itemReaderRepository;

    @InjectMocks
    CreateItemUseCase createItemUseCase;

    @DisplayName("신규 상품을 등록한다.")
    @Test
    void execute() {
        // given
        String name = "상품A";
        String description = "상품A 입니다.";
        CreateItemUseCase.Input input = new CreateItemUseCase.Input(name, description);
        Item newItem = Item.createNewItem(input.getName(), input.getDescription());
        Item savedItem = Item.builder()
                .id(1L)
                .name(input.getName())
                .description(input.getDescription())
                .quantity(0L)
                .build();

        given(itemReaderRepository.existsByName(input.getName())).willReturn(false);
        given(itemWriterRepository.save(newItem)).willReturn(savedItem);

        // when
        CreateItemUseCase.Output output = createItemUseCase.execute(input);
        
        // then
        Item actual = output.getItem();
        Assertions.assertThat(actual.getId()).isEqualTo(1L);
        Assertions.assertThat(actual.getName()).isEqualTo(name);
        Assertions.assertThat(actual.getDescription()).isEqualTo(description);
        Assertions.assertThat(actual.getQuantity()).isEqualTo(0L);
    }

    @DisplayName("신규 상품을 등록할때 중복된 상품 이름이 있으면 예외가 발생한다.")
    @Test
    void execute_shouldThrowExceptionWhenDuplicateItemNameExists() {
        // given
        String name = "상품A";
        String description = "상품A 입니다.";
        CreateItemUseCase.Input input = new CreateItemUseCase.Input(name, description);

        given(itemReaderRepository.existsByName(input.getName())).willReturn(true);

        // when & then
        Assertions.assertThatThrownBy(() -> createItemUseCase.execute(input))
                        .isInstanceOf(IllegalStateException.class)
                        .hasMessage("이미 존재하는 상품명 입니다.");
    }
}