package com.foodweek.demo.model;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI myOpenAPI() {
        // Configuration de base avec informations sur l'API
        return new OpenAPI()
        
                .addSecurityItem(new SecurityRequirement().
                        addList("Bearer Authentication"))
                 .components(new Components().addSecuritySchemes
                            ("Bearer Authentication", createAPIKeyScheme()))
                            
                .info(new Info()
                        .title("API de Mon Application")
                        .version("1.0")
                        .description("Documentation de l'API"));
    }
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
            .bearerFormat("JWT")
            .scheme("bearer");
    }
}