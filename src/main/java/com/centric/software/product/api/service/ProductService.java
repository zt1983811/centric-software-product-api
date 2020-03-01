package com.centric.software.product.api.service;

import com.centric.software.product.api.exception.ProductNotFoundException;
import com.centric.software.product.api.exception.RequestCategoryNotFound;
import com.centric.software.product.api.mapper.ProductMapper;
import com.centric.software.product.api.model.dto.ProductDTO;
import com.centric.software.product.api.model.entity.Product;
import com.centric.software.product.api.repository.CategoryRepository;
import com.centric.software.product.api.repository.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private ProductMapper productMapper;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    public List<ProductDTO> findByCategory(
            final String categoryName,
            final Integer pageNumber,
            final Integer pageSize,
            final String sortBy
    ) throws RequestCategoryNotFound {
        final var category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new RequestCategoryNotFound(categoryName));

        final var paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        return productRepository.findByCategory(category, paging).stream()
                .map(product -> productMapper.toProductDto(product))
                .collect(Collectors.toList());
    }

    public Product create(final ProductDTO productDto) {
        return productRepository.save(productMapper.toProduct(productDto));
    }

    public ProductDTO findById(UUID id) throws ProductNotFoundException {
        final var product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return productMapper.toProductDto(product);
    }
}
