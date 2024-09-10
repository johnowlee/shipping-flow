package com.shippingflow.core.domain.stock;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class StockTransaction {
    private Long id;
    private Stock stock;
    private long quantity;
    private StockTransactionType transactionType;
    private LocalDateTime transactionDateTime;

    @Builder
    private StockTransaction(Long id, Stock stock, long quantity, StockTransactionType transactionType, LocalDateTime transactionDateTime) {
        this.id = id;
        this.stock = stock;
        this.quantity = quantity;
        this.transactionType = transactionType;
        this.transactionDateTime = transactionDateTime;
    }

    public static StockTransaction createNewStockTransaction(Stock stock, long quantity, StockTransactionType transactionType, LocalDateTime transactionDateTime) {
        return builder()
                .id(null)
                .stock(stock)
                .quantity(quantity)
                .transactionType(transactionType)
                .transactionDateTime(transactionDateTime)
                .build();
    }

    public void assignedTo(Stock stock) {
        this.stock = stock;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        StockTransaction that = (StockTransaction) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
