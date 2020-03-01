package com.centric.software.product.api.service;

import com.centric.software.product.api.exception.ProductNotFoundException;
import com.centric.software.product.api.exception.RequestCategoryNotFound;
import com.centric.software.product.api.mapper.ProductMapper;
import com.centric.software.product.api.model.dto.ProductDTO;
import com.centric.software.product.api.model.entity.Category;
import com.centric.software.product.api.model.entity.Product;
import com.centric.software.product.api.repository.CategoryRepository;
import com.centric.software.product.api.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

// Unit test
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private Category category;

    @Test
    public void FindByCategory_CategoryFound_ProductsFound() throws RequestCategoryNotFound {

        //given
        givenCategoryFound();
        givenProductMapperToProductDto();
        givenProductsFoundByCategory();

        //when
        final var productService = new ProductService(productRepository, categoryRepository, productMapper);
        productService.findByCategory("beer", 0, 2, "createdAt");

        //then
        verify(categoryRepository, times(1)).findByName(anyString());
        verify(productRepository, times(1)).findByCategory(any(), any());
        verify(productMapper, times(1)).toProductDto(any());
    }

    @Test
    public void FindByCategory_CategoryNotFound_ExceptionThrow() throws RequestCategoryNotFound {

        //given
        givenCategoryNotFound();

        //when
        final var productService = new ProductService(productRepository, categoryRepository, productMapper);

        //then
        assertThrows(RequestCategoryNotFound.class,
                () -> productService.findByCategory("beer", 0, 2, "createdAt")
        );
        verify(categoryRepository, times(1)).findByName(anyString());
        verify(productRepository, times(0)).findByCategory(any(), any());
        verify(productMapper, times(0)).toProductDto(any());
    }

    @Test
    public void Create_SaveSuccess() {

        //given
        givenProductSaveSuccess();
        givenProductMapperToProduct();

        //when
        final var productService = new ProductService(productRepository, categoryRepository, productMapper);
        productService.create(ProductDTO.builder().build());

        //then
        verify(productRepository, times(1)).save(any());
        verify(productMapper, times(1)).toProduct(any());
    }

    @Test
    public void Create_SaveFailed_ExceptionThrow() {

        //given
        givenProductSaveFailed();
        givenProductMapperToProduct();

        //when
        final var productService = new ProductService(productRepository, categoryRepository, productMapper);

        //then
        assertThrows(RuntimeException.class, () -> productService.create(ProductDTO.builder().build()));
        verify(productRepository, times(1)).save(any());
        verify(productMapper, times(1)).toProduct(any());
    }

    @Test
    public void FindById_ProductFound_Success() throws ProductNotFoundException {

        //given
        givenProductFoundById();
        givenProductMapperToProductDto();

        //when
        final var productService = new ProductService(productRepository, categoryRepository, productMapper);
        productService.findById(UUID.randomUUID());

        //then
        verify(productRepository, times(1)).findById(any());
        verify(productMapper, times(1)).toProductDto(any());
    }

    @Test
    public void FindById_ProductNotFound_ExceptionThrow() throws ProductNotFoundException {

        //given
        givenProductNotFoundById();

        //when
        final var productService = new ProductService(productRepository, categoryRepository, productMapper);

        //then
        assertThrows(ProductNotFoundException.class, () -> productService.findById(UUID.randomUUID()));
        verify(productRepository, times(1)).findById(any());
        verify(productMapper, times(0)).toProductDto(any());
    }

    private void givenCategoryFound() {
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(category));
    }

    private void givenCategoryNotFound() {
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.empty());
    }

    private void givenProductSaveFailed() {
        when(productRepository.save(any())).thenThrow(new RuntimeException("Save failed"));
    }

    private void givenProductSaveSuccess() {
        when(productRepository.save(any())).thenReturn(Product.builder().build());
    }

    private void givenProductsFoundByCategory() {
        when(productRepository.findByCategory(any(), any())).thenReturn(List.of(Product.builder().build()));
    }

    private void givenProductFoundById() {
        when(productRepository.findById(any())).thenReturn(Optional.of(Product.builder().build()));
    }

    private void givenProductNotFoundById() {
        when(productRepository.findById(any())).thenReturn(Optional.empty());
    }

    private void givenProductMapperToProductDto() {
        when(productMapper.toProductDto(any())).thenReturn(ProductDTO.builder().build());
    }

    private void givenProductMapperToProduct() {
        when(productMapper.toProduct(any())).thenReturn(Product.builder().build());
    }
}