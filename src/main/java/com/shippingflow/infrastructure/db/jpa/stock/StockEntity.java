package com.shippingflow.infrastructure.db.jpa.stock;

import com.shippingflow.core.aggregate.item.local.Stock;
import com.shippingflow.core.usecase.aggregate.item.vo.StockVo;
import com.shippingflow.infrastructure.db.jpa.item.ItemEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "stock")
@Entity(name = "Stock")
public class StockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ItemEntity item;

    private Long quantity;

    @Builder
    private StockEntity(Long id, long quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public static StockEntity create(StockVo stock) {
        return of(null, stock.quantity());
    }

    public static StockEntity from(StockVo stockVo) {
        return of(stockVo.id(), stockVo.quantity());
    }

    public Stock toDomain() {
        return Stock.of(this.id, this.quantity);
    }

    public void assignedTo(ItemEntity item) {
        if (this.item != null) return;
        this.item = item;
    }

    private static StockEntity of(Long id, Long quantity) {
        return builder().id(id).quantity(quantity).build();
    }
}
