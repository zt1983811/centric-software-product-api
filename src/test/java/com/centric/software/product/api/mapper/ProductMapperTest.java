package com.centric.software.product.api.mapper;

import com.centric.software.product.api.model.dto.ProductDTO;
import com.centric.software.product.api.model.entity.Brand;
import com.centric.software.product.api.model.entity.Category;
import com.centric.software.product.api.model.entity.Product;
import com.centric.software.product.api.model.entity.Tag;
import com.centric.software.product.api.repository.BrandRepository;
import com.centric.software.product.api.repository.CategoryRepository;
import com.centric.software.product.api.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

// Unit test
@ExtendWith(MockitoExtension.class)
class ProductMapperTest {

    private final Brand brand = Brand.builder().id(1L).name("apple").build();

    private final Category category = Category.builder().id(1000L).name("laptop").build();

    private final Tag tag = Tag.builder().id(777L).name("10Core-i8").build();

    private final ProductDTO productDTO = ProductDTO.builder()
            .name("demo product")
            .description("description is wordless ;-)")
            .tags(List.of(tag.getName(), "AMD-super"))
            .brand(brand.getName())
            .category(category.getName())
            .build();

    private final Product product = Product.builder()
            .name("demo product")
            .description("description is wordless ;-)")
            .tags(Set.of(tag))
            .brand(brand)
            .category(category)
            .build();

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TagRepository tagRepository;

    private ProductMapper productMapper;

    @BeforeEach
    public void setup() {
        this.productMapper = new ProductMapper(brandRepository, categoryRepository, tagRepository);
    }

    @Test
    public void ToProduct_BrandNotFound_BrandIdIsNull() {

        //given
        givenBrandNotFound();
        givenCategoryFound();
        givenTagFound();

        //when
        final var product = productMapper.toProduct(productDTO);

        //then
        assertNull(product.getBrand().getId());
        assertNotNull(product.getCategory().getId());
        assertNotNull(product.getTags().iterator().next().getId());

        //verify
        verifyRepositoryFindByNameCalls(1, 1, 2);
    }

    @Test
    public void ToProduct_CategoryNotFound_CategoryIdIsNull() {

        //given
        givenBrandFound();
        givenCategoryNotFound();
        givenTagFound();

        //when
        final var product = productMapper.toProduct(productDTO);

        //then
        assertNotNull(product.getBrand().getId());
        assertNull(product.getCategory().getId());
        assertNotNull(product.getTags().iterator().next().getId());

        //verify
        verifyRepositoryFindByNameCalls(1, 1, 2);
    }

    @Test
    public void ToProduct_TagNotFound_TagIdIsNull() {

        //given
        givenBrandFound();
        givenCategoryFound();
        givenTagNotFound();

        //when
        final var product = productMapper.toProduct(productDTO);

        //then
        assertNotNull(product.getBrand().getId());
        assertNotNull(product.getCategory().getId());
        assertNull(product.getTags().iterator().next().getId());

        //verify
        verifyRepositoryFindByNameCalls(1, 1, 2);
    }

    @Test
    public void ToProductDto_ProductTypeReturn() {

        //when
        final var productDto = productMapper.toProductDto(product);

        //verify
        assertTrue(productDto instanceof ProductDTO);
        assertNotNull(productDto);
        assertEquals(product.getName(), productDto.getName());
        assertEquals(product.getDescription(), productDto.getDescription());
        assertEquals(product.getBrand().getName(), productDto.getBrand());
        assertEquals(product.getCategory().getName(), productDto.getCategory());
        assertEquals(product.getTags().size(), productDto.getTags().size());
        verifyRepositoryFindByNameCalls(0, 0, 0);
    }

    private void verifyRepositoryFindByNameCalls(int timeOfBrand, int timeOfCategory, int timeOfTag) {
        verify(brandRepository, times(timeOfBrand)).findByName(anyString());
        verify(categoryRepository, times(timeOfCategory)).findByName(anyString());
        verify(tagRepository, times(timeOfTag)).findByName(anyString());
    }

    private void givenBrandNotFound() {
        when(brandRepository.findByName(anyString())).thenReturn(Optional.empty());
    }

    private void givenBrandFound() {
        when(brandRepository.findByName(anyString())).thenReturn(Optional.of(brand));
    }


    private void givenCategoryNotFound() {
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.empty());
    }

    private void givenCategoryFound() {
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(category));
    }

    private void givenTagNotFound() {
        when(tagRepository.findByName(anyString())).thenReturn(Optional.empty());
    }

    private void givenTagFound() {
        when(tagRepository.findByName(anyString())).thenReturn(Optional.of(tag));
    }
}