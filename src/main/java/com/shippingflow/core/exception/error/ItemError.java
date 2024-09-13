package com.shippingflow.core.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemError implements ErrorSpec {
    ITEM_NAME_ALREADY_EXISTS(new IllegalStateException(), "이미 존재하는 상품명 입니다."),
    NOT_FOUND_ITEM(new IllegalStateException(), "존재하지 않는 상품 입니다."),


    NOT_FOUND_STOCK(new IllegalStateException(), "재고 정보가 존재하지 않습니다."),
    STOCK_SHORTAGE(new IllegalStateException(), "재고가 부족합니다."),


    INSUFFICIENT_QUANTITY(new IllegalArgumentException(), "입출고 수량은 최소 1개이상 이어야 합니다.");

    private final RuntimeException exceptionType;
    private final String message;
}