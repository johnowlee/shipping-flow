package com.shippingflow.core.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemError implements ErrorSpec {
    ITEM_NAME_ALREADY_EXISTS(new IllegalStateException(), "이미 존재하는 상품명 입니다.")
    ;

    private final RuntimeException exceptionType;
    private final String message;
}
