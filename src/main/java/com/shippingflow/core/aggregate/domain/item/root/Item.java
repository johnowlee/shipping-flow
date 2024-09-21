package com.shippingflow.core.aggregate.domain.item.root;

import com.shippingflow.core.aggregate.domain.item.dto.ItemAggregateDto;
import com.shippingflow.core.aggregate.domain.item.dto.ItemDto;
import com.shippingflow.core.aggregate.domain.item.dto.ItemWithStockDto;
import com.shippingflow.core.aggregate.domain.item.local.Stock;
import com.shippingflow.core.aggregate.domain.item.local.StockTransactionType;
import com.shippingflow.core.aggregate.vo.ItemVo;
import com.shippingflow.core.aggregate.vo.StockVo;
import com.shippingflow.core.exception.DomainException;
import com.shippingflow.core.exception.error.ItemError;
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
        if (isStockNull()) {
            initializeStock(quantity);
            return;
        }
        this.stock.increase(quantity);
    }

    public void decreaseStock(long quantity) {
        if (isStockNull()) {
            throw DomainException.from(ItemError.STOCK_SHORTAGE);
        }
        this.stock.decrease(quantity);
    }

    public void recordStockTransaction(StockTransactionType transactionType, long quantity, LocalDateTime transactionDateTime) {
        stock.recordTransaction(transactionType, quantity, transactionDateTime);
    }

    public ItemVo toVo() {
        StockVo stockVo = this.stock != null ? this.stock.toVo() : null;
        return new ItemVo(this.id, this.name, this.price, this.description, stockVo);
    }

    public static Item from(ItemDto dto) {
        return of(dto.id(), dto.name(), dto.price(), dto.description());
    }

    public static Item from(ItemWithStockDto dto) {
        Item item = from(dto.item());
        if (dto.hasStock()) {
            Stock stock = Stock.from(dto.stock());
            item.bind(stock);
        }
        return item;
    }

    public static Item from(ItemAggregateDto dto) {
        Stock stock = Stock.from(dto.stock());
        stock.addTransactionsFrom(dto.transactions());
        Item item = from(dto.item());
        item.bind(stock);
        return item;
    }

    public ItemDto toDto() {
        return ItemDto.of(this.id, this.name, this.price, this.description);
    }

    public ItemWithStockDto toWithStockDto() {
        return ItemWithStockDto.of(this.toDto(), this.stock.toDto());
    }

    public ItemAggregateDto toAggregateDto() {
        return ItemAggregateDto.of(this.toDto(), this.stock.toDto(), this.stock.transactionsToDtoList());
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

    private void initializeStock(long quantity) {
        this.stock = Stock.create(quantity);
        bind(this.stock);
    }

    private boolean isStockNull() {
        return this.stock == null;
    }
}
