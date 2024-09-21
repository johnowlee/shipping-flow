package com.shippingflow.infrastructure.db.jpa.item;

import com.shippingflow.core.aggregate.domain.item.dto.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "item")
@Entity(name = "Item")
public class ItemEntity {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String name;

    private Long price;

    private String description;

    @OneToOne(mappedBy = "item", cascade = CascadeType.ALL)
    private StockEntity stock;

    @Builder
    private ItemEntity(Long id, String name, Long price, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public static ItemEntity buildFrom(ItemAggregateDto itemAggregateDto) {
        ItemDto itemDto = itemAggregateDto.item();
        ItemEntity itemEntity = of(itemDto.id(), itemDto.name(), itemDto.price(), itemDto.description());

        StockDto stockDto = itemAggregateDto.stock();
        StockEntity stockEntity = StockEntity.buildFrom(stockDto);

        List<StockTransactionDto> transactions = itemAggregateDto.transactions();
        stockEntity.addTransactionsFrom(transactions);

        itemEntity.bind(stockEntity);
        return itemEntity;
    }

    public ItemWithStockDto toItemWithStockDto() {
        ItemDto itemDto = ItemDto.of(this.id, this.name, this.price, this.description);
        StockDto stockDto = this.stock.toStockDto();
        return ItemWithStockDto.of(itemDto, stockDto);
    }

    private void bind(StockEntity stock) {
        this.stock = stock;
        stock.bindTo(this);
    }

    private static ItemEntity create(String name, Long price, String description) {
        return of(null, name, price, description);
    }

    private static ItemEntity of(Long id, String name, Long price, String description) {
        return builder()
                .id(id)
                .name(name)
                .price(price)
                .description(description)
                .build();
    }
}
