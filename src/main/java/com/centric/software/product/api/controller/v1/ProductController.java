package com.centric.software.product.api.controller.v1;

import com.centric.software.product.api.exception.ProductNotFoundException;
import com.centric.software.product.api.exception.RequestCategoryNotFound;
import com.centric.software.product.api.model.dto.ProductDTO;
import com.centric.software.product.api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Tag(name = "products", description = "Products Endpoints Version 1")
@RestController
@RequestMapping(path = "/v1/products", produces = "application/json")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Find products")
    @GetMapping(path = "/")
    public List<ProductDTO> fetch(
            @RequestParam("category") String categoryName,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) throws RequestCategoryNotFound {
        try {
            return productService.findByCategory(categoryName, pageNumber, pageSize, sortBy);
        } catch (RequestCategoryNotFound | PropertyReferenceException ex) {
            log.error("EventType=BadRequestParamFound CategoryName={} SortBy={}", categoryName, sortBy);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @Operation(summary = "Add a product")
    @PostMapping(path = "/", consumes = "application/json")
    public ProductDTO create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Product Input", required = true)
            @RequestBody final ProductDTO productDto) throws ProductNotFoundException {
        final var product = this.productService.create(productDto);
        try {
            return this.productService.findById(product.getId());
        } catch (ProductNotFoundException ex) {
            log.error("EventType=ProductNotFoundAfterSave ProductId={} ProductName={}",
                    product.getId(), product.getName());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }
}
