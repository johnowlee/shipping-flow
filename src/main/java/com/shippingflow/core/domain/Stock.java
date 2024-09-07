package com.shippingflow.core.domain;

import java.time.LocalDateTime;

public class Stock {
    private Long id;
    private Item item;
    private LocalDateTime transactionDateTime;
    private StockTransactionType transactionType;
}
