package com.shippingflow.infrastructure.db.jpa.item;

import com.shippingflow.core.aggregate.domain.item.dto.StockTransactionDto;
import com.shippingflow.core.aggregate.domain.item.local.StockTransactionType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "stock_transaction")
@Entity(name = "StockTransaction")
public class StockTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_transaction_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private StockEntity stock;

    private long quantity;

    @Enumerated(EnumType.STRING)
    private StockTransactionType transactionType;

    private LocalDateTime transactionDateTime;

    @Builder
    private StockTransactionEntity(Long id, long quantity, StockTransactionType transactionType, LocalDateTime transactionDateTime) {
        this.id = id;
        this.quantity = quantity;
        this.transactionType = transactionType;
        this.transactionDateTime = transactionDateTime;
    }

    public static StockTransactionEntity createFrom(StockTransactionDto stockTransactionDto) {
        return create(stockTransactionDto.quantity(), stockTransactionDto.transactionType(), stockTransactionDto.transactionDateTime());
    }

    public void bindTo(StockEntity stockEntity) {
        this.stock = stockEntity;
    }

    private static StockTransactionEntity create(long quantity, StockTransactionType transactionType, LocalDateTime transactionDateTime) {
        return of(null, quantity, transactionType, transactionDateTime);
    }

    private static StockTransactionEntity of(Long id, long quantity, StockTransactionType transactionType, LocalDateTime transactionDateTime) {
        return builder()
                .id(id)
                .quantity(quantity)
                .transactionType(transactionType)
                .transactionDateTime(transactionDateTime)
                .build();
    }
}
