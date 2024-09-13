package com.shippingflow.core.aggregate.item.local;

import com.shippingflow.core.usecase.aggregate.item.vo.StockTransactionVo;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Objects;

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

    public static StockTransaction of(Long id, Stock stock, long quantity, StockTransactionType transactionType, LocalDateTime transactionDateTime) {
        return builder()
                .id(id)
                .stock(stock)
                .quantity(quantity)
                .transactionType(transactionType)
                .transactionDateTime(transactionDateTime)
                .build();
    }

    public static StockTransaction create(StockTransactionType transactionType, long quantity, LocalDateTime transactionDateTime) {
        return of(null, null, quantity, transactionType, transactionDateTime);
    }

    public void assignedTo(Stock stock) {
        this.stock = stock;
    }

    public StockTransactionVo toVo() {
        return new StockTransactionVo(this.id, this.stock.toVo(), this.quantity, this.transactionType, this.transactionDateTime);
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
