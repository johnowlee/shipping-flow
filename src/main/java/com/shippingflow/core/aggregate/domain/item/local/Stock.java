package com.shippingflow.core.aggregate.domain.item.local;

import com.shippingflow.core.aggregate.domain.item.repository.dto.StockDto;
import com.shippingflow.core.aggregate.domain.item.repository.dto.StockTransactionDto;
import com.shippingflow.core.aggregate.domain.item.root.Item;
import com.shippingflow.core.aggregate.vo.StockTransactionVo;
import com.shippingflow.core.aggregate.vo.StockVo;
import com.shippingflow.core.exception.DomainException;
import com.shippingflow.core.exception.error.ItemError;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
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

    public static Stock create(long quantity) {
        return builder().quantity(quantity).build();
    }

    public static Stock of(Long id, long quantity) {
        return Stock.builder().id(id).quantity(quantity).build();
    }

    public static Stock from(StockDto dto) {
        return of(dto.id(), dto.quantity());
    }

    public void bindTo(Item item) {
        this.item = item;
    }

    public void increase(long quantity) {
        this.quantity += quantity;
    }

    public void decrease(long quantity) {
        if (this.quantity < quantity) {
            throw DomainException.from(ItemError.STOCK_SHORTAGE);
        }
        this.quantity -= quantity;
    }

    public void recordTransaction(StockTransactionType transactionType, long quantity, LocalDateTime transactionDateTime) {
        if (quantity < 1) {
            throw DomainException.from(ItemError.INSUFFICIENT_QUANTITY);
        }
        StockTransaction transaction = StockTransaction.create(transactionType, quantity, transactionDateTime);
        addTransaction(transaction);
    }

    public void addTransaction(StockTransaction stockTransaction) {
        this.transactions.add(stockTransaction);
        stockTransaction.bindTo(this);
    }

    public void addTransactionsFrom(List<StockTransactionDto> stockTransactionDtoList) {
        stockTransactionDtoList.stream()
                .map(StockTransaction::from)
                .forEach(this::addTransaction);
    }

    public StockVo toVo() {
        List<StockTransactionVo> stockTransactionVoList = this.transactions.stream()
                .map(StockTransaction::toVo)
                .toList();
        return new StockVo(this.id, this.quantity, stockTransactionVoList);
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
