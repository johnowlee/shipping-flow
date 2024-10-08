package com.shippingflow.infrastructure.db.item.entity;

import com.shippingflow.core.domain.aggregate.item.dto.StockDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
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

    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL)
    private List<StockTransactionEntity> transactions = new ArrayList<>();

    private Long quantity;

    @Builder
    private StockEntity(Long id, long quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public static StockEntity buildFrom(StockDto stockDto) {
        return of(stockDto.id(), stockDto.quantity());
    }

    public StockDto toStockDto() {
        return StockDto.of(this.id, this.quantity);
    }

    public void bindTo(ItemEntity item) {
        this.item = item;
    }

    public void addTransaction(StockTransactionEntity stockTransaction) {
        this.transactions.add(stockTransaction);
        stockTransaction.bindTo(this);
    }

    public void addTransactions(List<StockTransactionEntity> stockTransactions) {
        stockTransactions.forEach(this::addTransaction);
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    private static StockEntity of(Long id, Long quantity) {
        return builder().id(id).quantity(quantity).build();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;

        if (!(object instanceof StockEntity stock)) {
            return false;
        }
        return this.getId() != null && Objects.equals(this.getId(), stock.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
