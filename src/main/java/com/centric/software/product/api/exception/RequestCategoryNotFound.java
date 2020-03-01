package com.centric.software.product.api.exception;

public class RequestCategoryNotFound extends Exception {

    public RequestCategoryNotFound(String name) {
        super("Request category " + name + " Not found");
    }
}
