package com.shippingflow.core.domain.stock;

import java.time.LocalDateTime;

public class StockTransaction {

    private Long id;
    private long quantity;
    private StockTransactionType transactionType;
    private LocalDateTime transactionDateTime;
    private Stock stock;
}
