package com.centric.software.product.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ProductDTO {

    @Schema(hidden = true)
    private UUID id;

    private String name;

    private String description;

    private String brand;

    private List<String> tags;

    @Schema(defaultValue = "abc")
    private String category;

    @Schema(hidden = true)
    private LocalDateTime createdAt;
}
