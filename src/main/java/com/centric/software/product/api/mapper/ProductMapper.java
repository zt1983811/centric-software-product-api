package com.centric.software.product.api.mapper;

import com.centric.software.product.api.model.dto.ProductDTO;
import com.centric.software.product.api.model.entity.Brand;
import com.centric.software.product.api.model.entity.Category;
import com.centric.software.product.api.model.entity.Product;
import com.centric.software.product.api.model.entity.Tag;
import com.centric.software.product.api.repository.BrandRepository;
import com.centric.software.product.api.repository.CategoryRepository;
import com.centric.software.product.api.repository.TagRepository;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ProductMapper {

    private BrandRepository brandRepository;
    private CategoryRepository categoryRepository;
    private TagRepository tagRepository;

    public ProductMapper(
            final BrandRepository brandRepository,
            final CategoryRepository categoryRepository,
            final TagRepository tagRepository
    ) {
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    public Product toProduct(final ProductDTO productDto) {
        final var brand = findBrandOrCreate(productDto.getBrand());
        final var category = findCategoryOrCreate(productDto.getCategory());
        final var tags = productDto.getTags().stream()
                .map(this::findTagOrCreate)
                .collect(Collectors.toSet());

        return Product.builder()
                .id(productDto.getId())
                .name(productDto.getName())
                .description(productDto.getDescription())
                .brand(brand)
                .category(category)
                .tags(tags)
                .build();
    }

    public ProductDTO toProductDto(Product product) {
        final var tags = product.getTags().parallelStream()
                .map(Tag::getName)
                .collect(Collectors.toList());

        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .brand(product.getBrand().getName())
                .category(product.getCategory().getName())
                .createdAt(product.getCreatedAt())
                .tags(tags).build();
    }

    private Brand findBrandOrCreate(final String brandName) {
        return brandRepository.findByName(brandName)
                .orElse(Brand.builder().name(brandName).build());
    }

    private Category findCategoryOrCreate(final String categoryName) {
        return categoryRepository.findByName(categoryName)
                .orElse(Category.builder().name(categoryName).build());
    }

    private Tag findTagOrCreate(final String tagName) {
        return tagRepository.findByName(tagName)
                .orElse(Tag.builder().name(tagName).build());
    }
}
