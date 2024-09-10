package com.shippingflow.core.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StockError implements ErrorSpec {
    NOT_FOUND_STOCK(new IllegalStateException(), "재고 정보가 존재하지 않습니다."),
    STOCK_SHORTAGE(new IllegalStateException(), "재고가 부족합니다.")
    ;

    private final RuntimeException exceptionType;
    private final String message;
}
