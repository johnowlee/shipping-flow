package com.shippingflow.infrastructure.db.jpa.item;

import com.shippingflow.core.domain.aggregate.item.root.Item;
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
    private ItemEntity(Long id, String name, Long price, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public static ItemEntity create(String name, Long price, String description) {
        return of(null, name, price, description);
    }

    public static ItemEntity createFrom(ItemVo itemVo) {
        ItemEntity itemEntity = create(itemVo.name(), itemVo.price(), itemVo.description());
        StockEntity stockEntity = StockEntity.create();
        itemEntity.bind(stockEntity);
        return itemEntity;
    }

    public Item toDomain() {
        Item item = Item.of(this.id, this.name, this.price, this.description);
        item.bind(this.stock.toDomain());
        return item;
    }

    private static ItemEntity of(Long id, String name, Long price, String description) {
        return builder()
                .id(id)
                .name(name)
                .price(price)
                .description(description)
                .build();
    }

    private void bind(StockEntity stock) {
        this.stock = stock;
        stock.bindTo(this);
    }
}
