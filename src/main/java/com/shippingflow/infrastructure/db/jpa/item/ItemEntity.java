package com.shippingflow.infrastructure.db.jpa.item;

import com.shippingflow.core.aggregate.item.root.Item;
import com.shippingflow.core.usecase.aggregate.item.vo.ItemVo;
import com.shippingflow.infrastructure.db.jpa.stock.StockEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private ItemEntity(Long id, String name, Long price, String description, StockEntity stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
    }

    private static ItemEntity of(Long id, String name, Long price, String description, StockEntity stock) {
        return builder()
                .id(id)
                .name(name)
                .price(price)
                .description(description)
                .stock(stock)
                .build();
    }

    public static ItemEntity create(ItemVo itemVo) {
        ItemEntity itemEntity = of(null, itemVo.name(), itemVo.price(), itemVo.description(), null);
        StockEntity stockEntity = StockEntity.create(itemVo.stock());
        itemEntity.assignStock(stockEntity);
        return itemEntity;
    }

    public Item toDomain() {
        this.assignStock(this.stock);
        return Item.of(this.id, this.name, this.price, this.description, this.stock.toDomain());
    }

    private void assignStock(StockEntity stock) {
        if (this.stock != null) return;
        this.stock = stock;
        stock.assignedTo(this);
    }
}
