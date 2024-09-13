package com.shippingflow.core.domain.aggregate.item.root;

import com.shippingflow.core.domain.aggregate.item.local.Stock;
import com.shippingflow.core.domain.aggregate.item.local.StockTransactionType;
import com.shippingflow.core.usecase.aggregate.item.vo.ItemVo;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Item {
    private Long id;
    private String name;
    private Long price;
    private String description;
    private Stock stock;

    @Builder
    private Item(Long id, String name, Long price, String description, Stock stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
    }

    public static Item of(Long id, String name, Long price, String description) {
        return builder()
                .id(id)
                .name(name)
                .price(price)
                .description(description)
                .build();
    }

    public static Item create(String name, Long price, String description) {
        return of(null, name, price, description);
    }

    public void bind(Stock stock) {
        this.stock = stock;
        stock.bindTo(this);
    }

    public void increaseStock(long quantity) {
        this.stock.increase(quantity);
    }

    public void decreaseStock(long quantity) {
        this.stock.decrease(quantity);
    }

    public void recordStockTransaction(StockTransactionType transactionType, long quantity, LocalDateTime transactionDateTime) {
        stock.recordTransaction(transactionType,quantity, transactionDateTime);
    }

    public ItemVo toVo() {
        return new ItemVo(this.id, this.name, this.price, this.description, this.stock.toVo());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Item item = (Item) object;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
