package com.shippingflow.infrastructure.db.jpa.item;

import com.shippingflow.core.domain.item.Item;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

    public static ItemEntity createNewFrom(Item item) {
        return new ItemEntity(null, item.getName(), item.getPrice(), item.getDescription());
    }

    public static ItemEntity from(Item item) {
        return new ItemEntity(item.getId(), item.getName(), item.getPrice(), item.getDescription());
    }

    public Item toDomain() {
        return Item.createItem(this.id, this.name, this.price, this.description);
    }
}
