package com.centric.software.product.api.exception;

import java.util.UUID;

public class ProductNotFoundException extends Exception {

    public ProductNotFoundException(UUID id) {
        super("Product ID" + id + " not found");
    }
}
