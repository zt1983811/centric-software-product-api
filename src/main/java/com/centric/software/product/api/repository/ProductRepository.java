package com.centric.software.product.api.repository;

import com.centric.software.product.api.model.entity.Category;
import com.centric.software.product.api.model.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends PagingAndSortingRepository<Product, UUID> {
    List<Product> findByCategory(Category category, Pageable page);
}