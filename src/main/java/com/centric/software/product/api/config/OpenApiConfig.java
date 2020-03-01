package com.centric.software.product.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI api() {
        return new OpenAPI().info(new Info()
                .title("Centric Software Product API")
                .version("version 1")
                .contact(new Contact().name("Tong Zhou").email("TongZhouMTL@gmail.com"))
                .description("This is product Spring Boot RESTful service to provide product create and search"));
    }
}
