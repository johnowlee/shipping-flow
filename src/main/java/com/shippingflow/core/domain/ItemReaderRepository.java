package com.shippingflow.core.domain;

public interface ItemReaderRepository {
    boolean existsByName(String name);
}
