package com.shippingflow.infrastructure.db.jpa.item;

import com.shippingflow.core.domain.aggregate.item.dto.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Getter
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

    public void bind(StockEntity stock) {
        this.stock = stock;
        stock.bindTo(this);
    }

    public static ItemEntity createFrom(ItemAggregateDto itemAggregateDto) {
        if (itemAggregateDto.isStockAbsent()) {
            ItemDto itemDto = itemAggregateDto.item();
            return buildFrom(itemDto);
        }
        return buildFrom(itemAggregateDto);
    }

    public static ItemEntity buildFrom(ItemAggregateDto itemAggregateDto) {
        ItemDto itemDto = itemAggregateDto.item();
        ItemEntity itemEntity = buildFrom(itemDto);

        StockDto stockDto = itemAggregateDto.stock();
        StockEntity stockEntity = StockEntity.buildFrom(stockDto);

        List<StockTransactionDto> transactions = itemAggregateDto.transactions();
        stockEntity.addTransactionsFrom(transactions);

        itemEntity.bind(stockEntity);
        return itemEntity;
    }

    public ItemWithStockDto toItemWithStockDto() {
        ItemDto itemDto = ItemDto.of(this.id, this.name, this.price, this.description);
        StockDto stockDto = checkStockPresence();
        return ItemWithStockDto.of(itemDto, stockDto);
    }

    private StockDto checkStockPresence() {
        return this.isStockPresent() ? this.stock.toStockDto() : null;
    }

    private boolean isStockPresent() {
        return this.stock != null;
    }

    private static ItemEntity buildFrom(ItemDto itemDto) {
        return of(itemDto.id(), itemDto.name(), itemDto.price(), itemDto.description());
    }

    private static ItemEntity of(Long id, String name, Long price, String description) {
        return builder()
                .id(id)
                .name(name)
                .price(price)
                .description(description)
                .build();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;

        if (!(object instanceof ItemEntity item)) {
            return false;
        }
        return this.getId() != null && Objects.equals(this.getId(), item.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
