package com.shippingflow.core.domain.stock;

import com.shippingflow.core.domain.item.Item;
import com.shippingflow.core.exception.DomainException;
import com.shippingflow.core.exception.error.StockError;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class Stock {
    private Long id;
    private Item item;
    private long quantity;
    private List<StockTransaction> transactions = new ArrayList<>();

    @Builder
    private Stock(Long id, long quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public static Stock createNewStock(Item item) {
        Stock stock = Stock.builder().build();
        stock.assignedTo(item);
        return stock;
    }

    public static Stock createStock(Long id, Item item, long quantity) {
        Stock stock = Stock.builder().id(id).quantity(quantity).build();
        stock.assignedTo(item);
        return stock;
    }

    public void assignedTo(Item item) {
        if (this.item != null) return;
        this.item = item;
    }

    public void addTransaction(StockTransaction transaction) {
        this.transactions.removeIf(tx -> tx.equals(transaction));
        this.transactions.add(transaction);
        transaction.assignedTo(this);
    }

    public void increase(long quantity) {
        this.quantity += quantity;
    }

    public void decrease(long quantity) {
        if (this.quantity < quantity) {
            throw DomainException.from(StockError.STOCK_SHORTAGE);
        }
        this.quantity -= quantity;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Stock stock = (Stock) object;
        return Objects.equals(id, stock.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
