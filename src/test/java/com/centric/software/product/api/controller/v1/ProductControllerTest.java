package com.centric.software.product.api.controller.v1;

import com.centric.software.product.api.exception.ProductNotFoundException;
import com.centric.software.product.api.exception.RequestCategoryNotFound;
import com.centric.software.product.api.model.dto.ProductDTO;
import com.centric.software.product.api.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

// Unit test
@SpringBootTest
class ProductControllerTest {

    @Autowired
    private ProductService productService;

    @Test
    public void Fetch_CategoryFound_ReturnProductsBySize() throws RequestCategoryNotFound {

        final var productController = new ProductController(productService);
        final var products = productController.fetch("beer", 0, 2, "createdAt");

        assertEquals(2, products.size());
        assertEquals("tshirt", products.get(0).getName());
        assertEquals("dirty tshirt", products.get(0).getDescription());
        assertEquals("adidas", products.get(0).getBrand());
        assertEquals("shoes", products.get(1).getName());
        assertEquals("special shoses", products.get(1).getDescription());
        assertEquals("nike", products.get(1).getBrand());
    }

    @Test
    public void Fetch_CategoryNotFound_400ExceptionThrow() throws RequestCategoryNotFound {

        final var productController = new ProductController(productService);
        assertThrows(ResponseStatusException.class,
                () -> productController.fetch("Not found category", 0, 2, "createdAt")
        );
    }

    @Test
    public void Create_SaveSuccess_ReturnSavedProduct() throws ProductNotFoundException {

        final var productController = new ProductController(productService);
        final var product = ProductDTO.builder()
                .name("new test product")
                .description("foosball table for play")
                .brand("nike")
                .category("pepsi")
                .tags(List.of("blue", "yellow"))
                .build();

        final var createdProduct = productController.create(product);
        assertEquals(product.getName(), createdProduct.getName());
        assertEquals(product.getBrand(), createdProduct.getBrand());
        assertEquals(product.getDescription(), createdProduct.getDescription());
        assertEquals(product.getCategory(), createdProduct.getCategory());
        assertEquals(product.getTags().size(), createdProduct.getTags().size());
    }
}