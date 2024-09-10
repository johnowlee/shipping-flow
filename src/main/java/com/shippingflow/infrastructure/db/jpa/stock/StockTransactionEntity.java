package com.shippingflow.infrastructure.db.jpa.stock;

import com.shippingflow.core.domain.stock.StockTransaction;
import com.shippingflow.core.domain.stock.StockTransactionType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

    public static StockTransactionEntity createNewFrom(StockTransaction transaction) {
        return new StockTransactionEntity(
                null,
                StockEntity.from(transaction.getStock()),
                transaction.getQuantity(),
                transaction.getTransactionType(),
                transaction.getTransactionDateTime()
        );
    }

    public StockTransaction toDomain() {
        return StockTransaction.createStockTransaction(
                this.id,
                this.stock.toDomain(),
                this.quantity,
                this.transactionType,
                this.transactionDateTime
        );
    }
}
