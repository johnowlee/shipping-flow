package com.shippingflow.core.usecase.common;

import java.time.LocalDateTime;

@FunctionalInterface
public interface ClockManager {
    LocalDateTime getNowDateTime();
}
