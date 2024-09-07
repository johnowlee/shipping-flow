package com.shippingflow.core.domain;

import lombok.Builder;

import java.util.Objects;

public class Customer {
    private Long id;
    private String name;

    @Builder
    private Customer(Long id, String name) {
        this.id = id;
        this.name = name;
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Customer customer = (Customer) object;
        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
