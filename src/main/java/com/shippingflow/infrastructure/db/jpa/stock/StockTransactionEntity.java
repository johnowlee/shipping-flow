package com.shippingflow.infrastructure.db.jpa.stock;

import com.shippingflow.core.domain.aggregate.item.local.StockTransaction;
import com.shippingflow.core.domain.aggregate.item.local.StockTransactionType;
import com.shippingflow.core.usecase.aggregate.item.vo.StockTransactionVo;
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

    public static StockTransactionEntity create(StockTransactionVo transactionVo) {
        return of(null, transactionVo.quantity(), transactionVo.transactionType(), transactionVo.transactionDateTime());
    }

    public StockTransaction toDomain() {
        return StockTransaction.of(
                this.id,
//                this.stock.toDomain(),
                this.quantity,
                this.transactionType,
                this.transactionDateTime
        );
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
