package com.shippingflow.infrastructure.db.jpa.item;

import com.shippingflow.core.aggregate.domain.item.dto.StockDto;
import com.shippingflow.core.aggregate.domain.item.dto.StockTransactionDto;
import com.shippingflow.core.aggregate.domain.item.local.Stock;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    public static StockEntity create() {
        return of(null, null);
    }

    public static StockEntity buildFrom(StockDto stockDto) {
        return of(stockDto.id(), stockDto.quantity());
    }

    public Stock toDomain() {
        return Stock.of(this.id, this.quantity);
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

    public void addTransactionsFrom(List<StockTransactionDto> stockTransactionDtoList) {
        stockTransactionDtoList.stream()
                .map(StockTransactionEntity::createFrom)
                .forEach(this::addTransaction);
    }

    private static StockEntity of(Long id, Long quantity) {
        return builder().id(id).quantity(quantity).build();
    }
}
