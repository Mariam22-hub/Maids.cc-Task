package com.library.librarySystem.config.swag;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Library System API",
                version = "1.0",
                description = "API documentation for the Library System"
        )
)
public class OpenApiConfig {
}