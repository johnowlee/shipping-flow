package com.shippingflow.infrastructure.db.jpa.stock;

import com.shippingflow.core.domain.stock.Stock;
import com.shippingflow.infrastructure.db.jpa.item.ItemEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "stock")
@Entity(name = "Stock")
public class StockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ItemEntity item;

    private long quantity;

    public static StockEntity createNewFrom(Stock stock) {
        return new StockEntity(null, ItemEntity.from(stock.getItem()), stock.getQuantity());
    }

    public static StockEntity from(Stock stock) {
        return new StockEntity(stock.getId(), ItemEntity.from(stock.getItem()), stock.getQuantity());
    }

    public Stock toDomain() {
        return Stock.createStock(this.id, this.item.toDomain(), this.quantity);
    }
}
