package com.centric.software.product.api.controller.v1;

import com.centric.software.product.api.exception.ProductNotFoundException;
import com.centric.software.product.api.exception.RequestCategoryNotFound;
import com.centric.software.product.api.model.dto.ProductDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Integration Test
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerIntegrationTest {

    private final static String HOST = "http://localhost:";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void Fetch_CategoryFound_ReturnProductsBySize() throws RequestCategoryNotFound {

        final var responseEntity = this.restTemplate.getForEntity(HOST + port + "/v1/products/?category=beer",
                ProductDTO[].class);
        final var products = Arrays.asList(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2, products.size());
        assertEquals("tshirt", products.get(0).getName());
        assertEquals("dirty tshirt", products.get(0).getDescription());
        assertEquals("adidas", products.get(0).getBrand());
        assertEquals("shoes", products.get(1).getName());
        assertEquals("special shoses", products.get(1).getDescription());
        assertEquals("nike", products.get(1).getBrand());
    }

    @Test
    public void Fetch_CategoryNotFound_BadRequest() throws RequestCategoryNotFound {
        final var responseEntity = this.restTemplate.getForEntity(HOST + port + "/v1/products/?category=NotFound",
                String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void Fetch_SortByUnkownFiled_BadRequest() throws RequestCategoryNotFound {
        final var responseEntity = this.restTemplate.getForEntity(HOST + port + "/v1/products/?category=beer&sortBy" +
                "=unkownField", String.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void Create_VaildInput_SaveSuccess() throws ProductNotFoundException {

        final var productInput = ProductDTO.builder()
                .name("new test product")
                .description("foosball table for play")
                .brand("nike")
                .category("pepsi")
                .tags(List.of("blue", "yellow"))
                .build();

        final var responseEntity = this.restTemplate.postForEntity(HOST + port + "/v1/products/", productInput,
                ProductDTO.class);

        final var productOutput = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(productInput.getName(), productOutput.getName());
        assertEquals(productInput.getBrand(), productOutput.getBrand());
        assertEquals(productInput.getDescription(), productOutput.getDescription());
        assertEquals(productInput.getCategory(), productOutput.getCategory());
        assertEquals(productInput.getTags().size(), productOutput.getTags().size());
    }


    @Test
    public void Create_BadInput_BadRequest() throws ProductNotFoundException {

        final var productInput = "abc";
        final var responseEntity = this.restTemplate.postForEntity(HOST + port + "/v1/products/", productInput,
                ProductDTO.class);

        assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, responseEntity.getStatusCode());
    }
}