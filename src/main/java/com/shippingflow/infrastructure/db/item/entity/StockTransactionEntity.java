package com.shippingflow.infrastructure.db.item.entity;

import com.shippingflow.core.domain.aggregate.item.dto.StockTransactionDto;
import com.shippingflow.core.domain.aggregate.item.model.local.StockTransactionType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;

        if (!(object instanceof StockTransactionEntity transaction)) {
            return false;
        }
        return this.getId() != null && Objects.equals(this.getId(), transaction.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
